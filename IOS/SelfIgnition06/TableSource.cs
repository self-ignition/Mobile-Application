using System;
using Foundation;
using UIKit;

namespace SelfIgnition06
{
	public class TableSource : UITableViewSource
	{
		string[] tableItems;
		string cellIdentifier = "TableCell";


		public TableSource(string[] items)
		{
			tableItems = items;
		}

		public override nint RowsInSection(UITableView tableview, nint section)
		{
			//throw new NotImplementedException();
			return tableItems.Length;
		}

		public override UITableViewCell GetCell(UITableView tableView, NSIndexPath indexPath)
		{
			//throw new NotImplementedException();
			UITableViewCell cell = tableView.DequeueReusableCell(cellIdentifier);
			if (cell == null)
				cell = new UITableViewCell(UITableViewCellStyle.Default, cellIdentifier);
			cell.TextLabel.Text = tableItems[indexPath.Row];
			return cell;
		}
	}
}
