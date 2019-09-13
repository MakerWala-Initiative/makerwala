﻿using System.Web.Mvc;

namespace WebApp.Areas.Admin
{
    public class AdminAreaRegistration : AreaRegistration
    {
        public override string AreaName
        {
            get
            {
                return "Admin";
            }
        }

        public override void RegisterArea(AreaRegistrationContext context)
        {
            context.MapRoute(
            "Admin_default",
            "Admin/{controller}/{action}/{id}",
             new { area = "Admin", controller = "Login", action = "Index", id = UrlParameter.Optional });
            // new { action = "Index", id = UrlParameter.Optional }

        }
    }
}