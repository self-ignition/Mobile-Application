using System;
using System.Collections.Generic;
using System.Threading;
using System.Windows.Forms;
using System.Net;
using System.Text.RegularExpressions;
using MySql.Data.MySqlClient;

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
                //if (i < 50)
                {
                    busy = true;
                    //to fix the problem of trying to get through the loop too quickly
                    if (i <= currentI)
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

            //When the program has finished downloading pages, wait for threads to finish. Then commit to db
            Thread.Sleep(60000);
            Console.WriteLine("Calling commit to db");
            CommitFilesToDB();
        }

        private void CommitFilesToDB()
        {
            string constring = "datasource=127.0.0.1;port=3306;username=root;password=;database=si_recipe;";
            MySqlConnection con = new MySqlConnection(constring);
            List<Recipe> clonedRecipies = new List<Recipe>();
            clonedRecipies.AddRange(list);

            Console.WriteLine("Recipes: " + list.Count);

            foreach (var r in clonedRecipies)
            {

                con.Open();

                try
                {
                    MySqlCommand command = new MySqlCommand("INSERT INTO author (name) VALUES (\"" + r.author + "\");", con);
                    command.ExecuteNonQuery();
                }
                catch (Exception e)
                {

                }

                try
                {
                    MySqlCommand command = new MySqlCommand("INSERT INTO recipe (title, preptime, cooktime, author, yield) VALUES (\"" + r.Title + "\", \"" + r.prepTime + "\", \"" + r.cookTime + "\", \"" + r.author + "\", \"" + r.yield + "\" );", con);
                    Console.WriteLine(command.CommandText);
                    command.ExecuteNonQuery();
                }
                catch (Exception e)
                {
                    Console.WriteLine("Exception: " + e.Message);
                }

                int ingreNo = 0;
                foreach (var s in r.ingredients)
                {
                    try
                    {
                        if (s.Length >= 1)
                        {
                            string ingre = "INSERT INTO ingredient (num, title, information) VALUES (\"" + ingreNo + "\", \"" + r.Title + "\", \"" + s + "\");";
                            ingreNo++;
                            MySqlCommand command = new MySqlCommand(ingre, con);
                            Console.WriteLine(command.CommandText);
                            command.ExecuteNonQuery();
                        }
                        else
                        {
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine("Exception: " + e.Message);
                    }
                }

                int stepNo = 0;
                foreach (var s in r.method)
                {
                    try
                    {
                        if (s.Length >= 1)
                        {
                            string step = "INSERT INTO step (step, title, information) VALUES (\"" + stepNo + "\", \"" + r.Title + "\", \"" + s + "\");";
                            stepNo++;
                            MySqlCommand command = new MySqlCommand(step, con);
                            command.ExecuteNonQuery();
                        }
                        else
                        {
                            continue;
                        }
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine("Exception: " + e.Message);
                    }
                }

                con.Close();
            }

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
                        Regex re = new Regex(@"itemprop=""(\w+)""[^>]*>([^<]*)(?:<[^>]*>)?([^<]*)");
                        MatchCollection mc = re.Matches(page);
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
                                        string ingredient = "";
                                        for (int i = 2; i < m.Groups.Count; i++)
                                        {
                                            ingredient += m.Groups[i].Value;

                                        }
                                        recipe.ingredients.Add(ingredient);
                                        break;
                                    case "recipeInstructions":
                                        string method = "";
                                        for (int i = 2; i < m.Groups.Count; i++)
                                        {
                                            method += m.Groups[i].Value;
                                        }
                                        recipe.method.Add(method);
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