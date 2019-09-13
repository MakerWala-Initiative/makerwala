
using Data.Repository;
using Data.Services;
using Data.Repository.Services;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApp.Areas.Admin.Filters;
using WebApp.Utility;
using Data.Utility;
using System.Xml;
namespace WebApp.Areas.Admin.Controllers
{
    public partial class CountryController : AdminSessionController
    {
        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "country list", "Information", SessionFacade.AdminUserSession.teacherid);
           
            return View();
        }
        public ActionResult CountryForm(string id = "0")
        {
            ViewBag.Block_Id = id;
            return View();
        }


        public JsonResult Get_Country_List()
        {
            var CountryList = _IMaster_Repository.Get_Country_List("");

            var jsonResult = Json(new
            {
                aaData = CountryList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


        public JsonResult SaveUpdate_Delete_Country(tcountry Co, bool isDelete = false)
        {
          

            Co.createddate = DateTime.Now;
            Co.createdby = SessionFacade.AdminUserSession.teacherid;
            Co.updateddate = DateTime.Now;
            Co.updatedby = SessionFacade.AdminUserSession.teacherid;

            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(Co);
            xmlStr += "<country>" + xmlProduct.DocumentElement.InnerXml + "</country>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdate_Delete_Country(xmlStr, isDelete, out OutIdentity);
            if (RetrunValue > 0)
            {
                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "country-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (Co.countryid > 0)
                    {
                        CommonMethods.activitylogs("web", "country entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "country entry-add", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                }
            }
            var jsonResult = Json(new
            {
                aaData = RetrunValue
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }

    }
}