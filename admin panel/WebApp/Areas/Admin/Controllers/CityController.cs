
using Data.Repository;
using Data.Services;
using Data.Utility;
using Data.Repository.Services;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Xml;
using WebApp.Areas.Admin.Filters;
using WebApp.Utility;
namespace WebApp.Areas.Admin.Controllers
{
    public partial class CityController : AdminSessionController
    {
        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "city list", "Information", SessionFacade.AdminUserSession.teacherid);
           
            return View();
        }
        public ActionResult CityForm(string id = "0")
        {
            return View();
        }
        public JsonResult Get_city_List(int Country_Id = 0, int State_Id = 0)
        {
            

            var StateList = _IMaster_Repository.Get_City_List(Country_Id, State_Id);

            var jsonResult = Json(new
            {
                aaData = StateList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }

        public JsonResult SaveUpdate_Delete_City(Get_City_List_Result ct, bool isDelete = false)
        {

          

            ct.createddate = DateTime.Now;
            ct.createdby = SessionFacade.AdminUserSession.teacherid;
            ct.updateddate = DateTime.Now;
            ct.updatedby = SessionFacade.AdminUserSession.teacherid;

            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(ct);
            xmlStr += "<city>" + xmlProduct.DocumentElement.InnerXml + "</city>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_City(xmlStr, isDelete, out OutIdentity);

            if (RetrunValue > 0)
            {
                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "city-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (ct.cityid > 0)
                    {
                        CommonMethods.activitylogs("web", "city entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "city entry-add", "Information", SessionFacade.AdminUserSession.teacherid);
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