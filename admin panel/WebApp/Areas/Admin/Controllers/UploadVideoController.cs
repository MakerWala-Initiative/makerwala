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
    public class UploadVideoController : AdminSessionController
    {
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/UploadVideo/
        public ActionResult Index(string id = "")
        {
            CommonMethods.activitylogs("web", "video list", "Information", SessionFacade.AdminUserSession.teacherid);

            return View();
        }
        public ActionResult Uploadform()
        {
            return View();
        }

        public JsonResult Get_UploadVideo_List(SearchFilter obj)
        {
            int numberOfObjectsPerPage = 0;
            obj.Search = obj.Search == null ? "" : obj.Search;

            //if (SessionFacade.AdminUserSession.userroleid != 1)
            //{
            //    obj.Search += " and vd.teacherid=" + SessionFacade.AdminUserSession.teacherid;
            //}


            var UploadVideoList = _IMaster_Repository.Get_UploadVideo_List(obj.Search, obj.pageIndex, 0, out numberOfObjectsPerPage).ToList();

            var jsonResult = Json(new
            {
                aaData = UploadVideoList,
                totalRecords = numberOfObjectsPerPage
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


        public JsonResult SaveUpdate_Delete_UploadVideo(Get_UploadVideo_List_Result vd, bool isDelete = false)
        {


            vd.createddate = DateTime.Now;
            vd.createdby = SessionFacade.AdminUserSession.teacherid;
            vd.updateddate = DateTime.Now;
            vd.updatedby = SessionFacade.AdminUserSession.teacherid;

            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(vd);
            xmlStr += "<videodata>" + xmlProduct.DocumentElement.InnerXml + "</videodata>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_UploadVideo(xmlStr, isDelete, out OutIdentity);

            if (RetrunValue > 0)
            {
                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "video-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (vd.videoid > 0)
                    {
                        CommonMethods.activitylogs("web", "video entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "video entry-add", "Information", SessionFacade.AdminUserSession.teacherid);
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