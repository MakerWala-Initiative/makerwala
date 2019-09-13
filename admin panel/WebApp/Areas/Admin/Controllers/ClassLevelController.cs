
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
    public partial class ClassLevelController : AdminSessionController
    {
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "class list", "Information", SessionFacade.AdminUserSession.teacherid);

            return View();
        }
        public ActionResult ClassLevelForm(string id = "0")
        {

            ViewBag.Block_Id = id;
            return View();
        }


        public JsonResult Get_Class_List()
        {
            var ClasslevelList = _IMaster_Repository.Get_Class_List();

            var jsonResult = Json(new
            {
                aaData = ClasslevelList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


        public JsonResult SaveUpdate_Delete_Class(tclasslevel cl, bool isDelete = false)
        {


            cl.createddate = DateTime.Now;
            cl.createdby = SessionFacade.AdminUserSession.teacherid;
            cl.updateddate = DateTime.Now;
            cl.updatedby = SessionFacade.AdminUserSession.teacherid;


            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(cl);
            xmlStr += "<classlevel>" + xmlProduct.DocumentElement.InnerXml + "</classlevel>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_Class(xmlStr, isDelete, out OutIdentity);
            if (RetrunValue > 0)
            {

                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "class-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (cl.classid > 0)
                    {
                        CommonMethods.activitylogs("web", "class entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "class entry -add", "Information", SessionFacade.AdminUserSession.teacherid);
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