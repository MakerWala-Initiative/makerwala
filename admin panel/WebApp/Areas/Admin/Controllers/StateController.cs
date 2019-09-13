using WebApp.Areas.Admin.Filters;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Data.Repository;
using Data.Services;
using Model;
using WebApp.Utility;
using Data.Repository.Services;
using System.Xml;
using Data.Utility;

namespace WebApp.Areas.Admin.Controllers
{
    public partial class StateController : AdminSessionController
    {
        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "state list", "Information", SessionFacade.AdminUserSession.teacherid);


            return View();
        }
        public ActionResult StateForm(string id = "0")
        {
            ViewBag.Block_Id = id;
            return View();
        }

        public JsonResult Get_State_List(int Country_Id = 0)
        {
            var StateList = _IMaster_Repository.Get_State_List(Country_Id);

            var jsonResult = Json(new
            {
                aaData = StateList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }

        public JsonResult SaveUpdate_Delete_State(Get_State_List_Result st, bool isDelete = false)
        {




            st.createddate = DateTime.Now;
            st.createdby = SessionFacade.AdminUserSession.teacherid;
            st.updateddate = DateTime.Now;
            st.updatedby = SessionFacade.AdminUserSession.teacherid;

            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(st);
            xmlStr += "<state>" + xmlProduct.DocumentElement.InnerXml + "</state>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_State(xmlStr, isDelete, out OutIdentity);

            if (RetrunValue > 0)
            {
                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "state-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (st.stateid > 0)
                    {
                        CommonMethods.activitylogs("web", "state entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "state entry-add", "Information", SessionFacade.AdminUserSession.teacherid);

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