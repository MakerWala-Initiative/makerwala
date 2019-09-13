
using Data.Repository;
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
    public partial class TypeController : AdminSessionController
    {
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "type list", "Information", SessionFacade.AdminUserSession.teacherid);

            return View();
        }
        public ActionResult TypeForm()
        {
            return View();
        }

        public JsonResult Get_Type_List()
        {
            var ClasslevelList = _IMaster_Repository.Get_type_List("");

            var jsonResult = Json(new
            {
                aaData = ClasslevelList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


        public JsonResult SaveUpdate_Delete_Type(ttype Ty, bool isDelete = false)
        {


            Ty.createddate = DateTime.Now;
            Ty.createdby = SessionFacade.AdminUserSession.teacherid;
            Ty.updateddate = DateTime.Now;
            Ty.updatedby = SessionFacade.AdminUserSession.teacherid;


            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(Ty);
            xmlStr += "<type>" + xmlProduct.DocumentElement.InnerXml + "</type>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_type(xmlStr, isDelete, out OutIdentity);

            if (RetrunValue > 0)
            {
                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "type-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (Ty.typeid > 0)
                    {
                        CommonMethods.activitylogs("web", "type entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "type entry-add", "Information", SessionFacade.AdminUserSession.teacherid);
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