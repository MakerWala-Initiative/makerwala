
using Data.Repository;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApp.Areas.Admin.Filters;
using WebApp.Utility;
namespace WebApp.Areas.Admin.Controllers
{
    public partial class DashboardController : AdminSessionController
    {

        IDashboard_Repository _IDashboard_Repository = new Dashboard_Repository(new dataEntities());

        // GET: /Admin/Dashboard/
        public ActionResult Index()
        {
            return View();
        }



        public JsonResult getLineChartData(string chartType, string sSearch)
        {

            if (SessionFacade.AdminUserSession.userroleid != 1)
            {
                if (chartType == "ACTIVITYLOG")
                {
                    sSearch += " and a.userid=" + SessionFacade.AdminUserSession.teacherid;
                }
                else
                {
                    sSearch += " and a.teacherid=" + SessionFacade.AdminUserSession.teacherid;
                }
            }

            List<object> iData = new List<object>();
            List<string> labels = new List<string>();
            List<int> visits = new List<int>();

            var chart = _IDashboard_Repository.Get_Dashboard_Chart_data(chartType, sSearch);

            foreach (var item in chart)
            {
                labels.Add (Convert.ToDateTime(item.Dates).ToString("dd MMM yy"));
                visits.Add(Convert.ToInt16(item.Visits));
            }
            iData.Add(labels);
            iData.Add(visits);

            var jsonResult = Json(new
            {
                aaData = iData,
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


    }
}