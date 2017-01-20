using Foundation;
using System;
using UIKit;

namespace SelfIgnition06
{
    public partial class FirstTableViewController : UITableViewController
    {
        public FirstTableViewController (IntPtr handle) : base (handle)
        {
        }

		public override void ViewDidLoad()
		{
			base.ViewDidLoad();

			string[] data = new string[] { "Cell 1", "Cell 2", "Cell 3", "Cell 4" };

			UITableView _table;

			_table = new UITableView
			{
				Frame = new CoreGraphics.CGRect(0, 30, View.Bounds.Width, View.Bounds.Height - 30),
				Source = new TableSource(data)
			};

			View.AddSubview(_table);
		}
    }
}