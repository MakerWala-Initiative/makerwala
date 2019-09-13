
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApp.Utility
{
    public class SessionFacade
    {
        # region Private Constants
        //---------------------------------------------------------------------
        private const string AdminLoggedIn = "AdminLoggedIn";


        //---------------------------------------------------------------------

        # endregion

        # region Public Properties
        public static Get_User_Login_Result AdminUserSession
        {
            get
            {
                Get_User_Login_Result _Tracker_User_Login = (Get_User_Login_Result)HttpContext.Current.Session[AdminLoggedIn];
                return _Tracker_User_Login;
            }
            set
            {
                HttpContext.Current.Session[AdminLoggedIn] = value;
            }
        }



    }
        #endregion
}
