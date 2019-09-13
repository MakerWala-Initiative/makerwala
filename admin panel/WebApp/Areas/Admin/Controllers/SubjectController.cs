
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
    public partial class SubjectController : AdminSessionController
    {
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            return View();
        }
        public ActionResult SubjectForm(string id = "0")
        {
            ViewBag.Block_Id = id;
            return View();
        }




        public JsonResult Get_subjectforClass_List(string classid = "0")
        {
            var CountryList = _IMaster_Repository.Get_subjectforClass_List(classid);

            var jsonResult = Json(new
            {
                aaData = CountryList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


        public JsonResult Get_subjects_List()
        {
            var CountryList = _IMaster_Repository.Get_subject_List();

            var jsonResult = Json(new
            {
                aaData = CountryList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }

        public JsonResult Get_subject_List(String Search = "")
        {
            var SchoolList = _IMaster_Repository.Get_subject_List("", Search).ToList();

            var jsonResult = Json(new
            {
                aaData = SchoolList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


        public JsonResult SaveUpdate_Delete_Subject(Get_Subject_List_Result su, bool isDelete = false)
        {


            su.createddate = DateTime.Now;
            su.createdby = SessionFacade.AdminUserSession.teacherid;
            su.updateddate = DateTime.Now;
            su.updatedby = SessionFacade.AdminUserSession.teacherid;


            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(su);
            xmlStr += "<subject>" + xmlProduct.DocumentElement.InnerXml + "</subject>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_Subject(xmlStr, isDelete, out OutIdentity);

            if (RetrunValue > 0)
            {
                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "subject-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (su.subjectid > 0)
                    {
                        CommonMethods.activitylogs("web", "subject entery-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "subject entry-add", "Information", SessionFacade.AdminUserSession.teacherid);

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