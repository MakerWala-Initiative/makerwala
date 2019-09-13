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
    public partial class BlockController : AdminSessionController
    {
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "block list", "Information", SessionFacade.AdminUserSession.teacherid);
            return View();
        }
        public ActionResult BlockForm(string id = "0")
        {
            ViewBag.Block_Id = id;
            return View();
        }

        public JsonResult Get_block_List(string Search = "")
        {
            if (SessionFacade.AdminUserSession.userroleid != 1)
            {
                Search += " and b.blockid='" + SessionFacade.AdminUserSession.blockid + "'";
            }

            var StateList = _IMaster_Repository.Get_block_List(Search);

            var jsonResult = Json(new
            {
                aaData = StateList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }

        public JsonResult SaveUpdate_Delete_Block(Get_Block_List_Result bk, bool isDelete = false)
        {
            bk.createddate = DateTime.Now;
            bk.createdby = SessionFacade.AdminUserSession.teacherid;
            bk.updateddate = DateTime.Now;
            bk.updatedby = SessionFacade.AdminUserSession.teacherid;

            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(bk);
            xmlStr += "<block>" + xmlProduct.DocumentElement.InnerXml + "</block>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_Block(xmlStr, isDelete, out OutIdentity);

            if (RetrunValue > 0)
            {
                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "block-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (bk.blockid > 0)
                    {
                        CommonMethods.activitylogs("web", "block entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "block entry-add", "Information", SessionFacade.AdminUserSession.teacherid);
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