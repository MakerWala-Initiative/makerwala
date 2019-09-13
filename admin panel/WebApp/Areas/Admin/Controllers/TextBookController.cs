
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
    public partial class TextBookController : AdminSessionController
    {
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "textbook list", "Information", SessionFacade.AdminUserSession.teacherid);

            return View();
        }
        public ActionResult TextBookForm(string id = "0")
        {
            ViewBag.Block_Id = id;
            return View();
        }

        public JsonResult Get_Textbook_List(String Search)
        {
            var TeacherList = _IMaster_Repository.Get_Textbook_List(Search).ToList();

            var jsonResult = Json(new
            {
                aaData = TeacherList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }

        public JsonResult SaveUpdate_Delete_Textbook(Get_TextBook_List_Result tb, bool isDelete = false)
        {



            tb.createddate = DateTime.Now;
            tb.createdby = SessionFacade.AdminUserSession.teacherid;
            tb.updateddate = DateTime.Now;
            tb.updatedby = SessionFacade.AdminUserSession.teacherid;

            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(tb);
            xmlStr += "<textbook>" + xmlProduct.DocumentElement.InnerXml + "</textbook>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_textbook(xmlStr, isDelete, out OutIdentity);


            if (RetrunValue > 0)
            {
                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "textbook-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (tb.textbookid > 0)
                    {
                        CommonMethods.activitylogs("web", "textbook entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "textbook entry-add", "Information", SessionFacade.AdminUserSession.teacherid);

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