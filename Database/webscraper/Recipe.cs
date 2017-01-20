using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace webscraper
{
    public class Recipe
    {
        public string Title = "not assigned";
        public string prepTime = "not assigned";
        public string cookTime = "not assigned";
        public string author = "not assigned";
        public string yield = "not assigned";
        public List<string> ingredients = new List<string>();
        public List<string> method = new List<string>();
    }
}
