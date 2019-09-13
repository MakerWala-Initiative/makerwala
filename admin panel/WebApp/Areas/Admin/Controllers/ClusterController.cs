
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
    public partial class ClusterController : AdminSessionController
    {
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "cluster list", "Information", SessionFacade.AdminUserSession.teacherid);
           
            return View();
        }
        public ActionResult ClusterForm(string id = "0")
        {
            ViewBag.Block_Id = id;
            return View();
        }

        public JsonResult Get_Cluster_List()
        {
            var ClasslevelList = _IMaster_Repository.Get_cluster_List("");

            var jsonResult = Json(new
            {
                aaData = ClasslevelList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


        public JsonResult SaveUpdate_Delete_Cluster(tcluster cl, bool isDelete = false)
        {


            cl.createddate = DateTime.Now;
            cl.createdby = SessionFacade.AdminUserSession.teacherid;
            cl.updateddate = DateTime.Now;
            cl.updatedby = SessionFacade.AdminUserSession.teacherid;


            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(cl);
            xmlStr += "<cluster>" + xmlProduct.DocumentElement.InnerXml + "</cluster>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_cluster(xmlStr, isDelete, out OutIdentity);
            if (RetrunValue > 0)
            {

                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "cluster-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (cl.clusterid > 0)
                    {
                        CommonMethods.activitylogs("web", "cluster entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "cluster entry-add", "Information", SessionFacade.AdminUserSession.teacherid);
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