using Data.Repository;
using Data.Repository.Services;
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
    public class activitylogController : AdminSessionController
    {
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/activitylog/
        public ActionResult Index(string id = "")
        {
            return View();
        }

        public JsonResult Get_Activitylogs_List(SearchFilter obj)
        {
            obj.Search = obj.Search == null ? "" : obj.Search;

            if (SessionFacade.AdminUserSession.userroleid != 1)
            {
                obj.Search += " and a.userid=" + SessionFacade.AdminUserSession.teacherid;
            }

            

            int numberOfObjectsPerPage = 0;

            var UploadVideoList = _IMaster_Repository.Get_activitylog_List(obj.Search, obj.pageIndex, obj.PageSize, out numberOfObjectsPerPage).ToList();

            var jsonResult = Json(new
            {
                aaData = UploadVideoList,
                totalRecords = numberOfObjectsPerPage
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }
    }
}