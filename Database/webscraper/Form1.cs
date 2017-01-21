using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;
using System.Windows.Forms;
using System.Net;
using System.Text.RegularExpressions;
using System.Net.Http;
using System.Web;
using System.IO;
using System.Data;
using System.Data.SqlClient;

namespace webscraper
{
    public partial class Form1 : Form
    {
        public List<Recipe> list = new List<Recipe>();
        List<string> recipes = new List<string>();
        List<Thread> threads = new List<Thread>();

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            Thread t = new Thread(() => BeginDownload());
            t.Start();
        }

        private void BeginDownload()
        {
            recipes = DownloadSitemap("http://www.bbc.co.uk/food/sitemap.xml");
            bool busy = true;
            int i = 0;
            int numberOfThreads = 50;
            int currentI = numberOfThreads;
            Console.WriteLine("Number of recipe links: " + recipes.Count.ToString());

            while (busy)
            {
                if (threads.Count < numberOfThreads)
                {
                    string uri = "http://www.bbc.co.uk/food/recipes/" + recipes.ToArray()[i];
                    Thread t = new Thread(() => DownloadContent(uri));
                    t.Start();
                    threads.Add(t);
                }
                else
                {
                    List<Thread> toRemove = new List<Thread>();
                    foreach (Thread t in threads)
                    {
                        if (!t.IsAlive)
                        {
                            toRemove.Add(t);
                        }
                    }
                    foreach (Thread t in toRemove)
                    {
                        currentI++;
                        threads.Remove(t);
                    }
                    toRemove.Clear();
                }

                if (i < recipes.Count - 1)
                {
                    busy = true;
                    //to fix the problem of trying to get through the loop too quickly
                    if(i <= currentI)
                    {
                        i++;
                        Console.WriteLine("I: " + i + " out of " + recipes.Count.ToString());
                    }
                }
                else
                {
                    busy = false;
                }

            }

            CommitFilesToDB();
        }

        private void CommitFilesToDB()
        {
            SqlConnection con = new SqlConnection();
        }

        private void DownloadContent(string uri)
        {
            //Variables which are outside the using statement, because i'll want these later.
            List<string> recipies = new List<string>();
            Recipe recipe = new Recipe();
            string page = "";


            using (WebClient client = new WebClient())
            {
                try
                {
                    page = client.DownloadString(uri);

                    if (page != "")
                    {
                        MatchCollection mc = Regex.Matches(page, @"itemprop=""(\w+)""[^>]*>([^<]+)<[^>]+>([^<]+)?[</a>]?(.*)<");
                        foreach (Match m in mc)
                        {
                            if (m.Success)
                            {
                                switch (m.Groups[1].Value)
                                {
                                    case "name":
                                        recipe.Title = m.Groups[2].Value;
                                        break;
                                    case "recipeYield":
                                        recipe.yield = m.Groups[2].Value;
                                        break;
                                    case "author":
                                        recipe.author = m.Groups[2].Value;
                                        break;
                                    case "ingredients":
                                        for (int i = 2; i < m.Groups.Count; i++)
                                        {
                                            recipe.ingredients.Add(m.Groups[i].Value);
                                        }
                                        break;
                                    case "recipeInstructions":
                                        for (int i = 2; i < m.Groups.Count; i++)
                                        {
                                            recipe.method.Add(m.Groups[i].Value);
                                        }
                                        break;
                                    case "prepTime":
                                        recipe.prepTime = m.Groups[2].Value;
                                        break;
                                    case "cookTime":
                                        recipe.cookTime = m.Groups[2].Value;
                                        break;
                                }
                            }
                        }
                        list.Add(recipe);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message + "\n Failed to load page: " + uri);
                }
            }
        }

        private List<string> DownloadSitemap(string uri)
        {
            //Variables which are outside the using statement, because i'll want these later.
            List<string> recipies = new List<string>();
            string sitemap;

            //use a webclient to download the sitemap as a string.
            using (WebClient client = new WebClient())
            {
                sitemap = client.DownloadString(uri);
            }

            //REGEX to break the string down into what i want.
            MatchCollection mc = Regex.Matches(sitemap, "recipes/(\\w+)<");
            foreach (Match m in mc)
            {
                if (m.Success)
                {
                    recipies.Add(m.Groups[1].Captures[0].Value);
                }
            }

            return recipies;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            List<Recipe> r = new List<Recipe>();
            r.AddRange(list);
            listBox1.Items.Clear();

            foreach (var m in r)
            {
                listBox1.Items.Add(m.Title);
            }
        }
    }
}