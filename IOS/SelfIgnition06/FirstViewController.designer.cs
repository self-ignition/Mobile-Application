// WARNING
//
// This file has been generated automatically by Xamarin Studio from the outlets and
// actions declared in your storyboard file.
// Manual changes to this file will not be maintained.
//
using Foundation;
using System;
using System.CodeDom.Compiler;

namespace SelfIgnition06
{
    [Register ("FirstViewController")]
    partial class FirstViewController
    {
        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UIButton button1 { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UIButton button2 { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UITextField textField1 { get; set; }

        void ReleaseDesignerOutlets ()
        {
            if (button1 != null) {
                button1.Dispose ();
                button1 = null;
            }

            if (button2 != null) {
                button2.Dispose ();
                button2 = null;
            }

            if (textField1 != null) {
                textField1.Dispose ();
                textField1 = null;
            }
        }
    }
}