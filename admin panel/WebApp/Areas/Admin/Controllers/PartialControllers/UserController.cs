
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Xml;
using WebApp.Areas.Admin.Filters;

namespace WebApp.Areas.Admin.Controllers
{
    public partial class UserController : AdminSessionController
    {
        //public JsonResult Get_User_List(SearchFilter SearchObj)
        //{
        //    int numberOfObjectsPerPage = 0;
        //    var sSearch = SearchObj.Search == null ? "" : SearchObj.Search;
        //    var produt = _IMaster_Repository.Get_User_List(sSearch, SearchObj.User_Id, SearchObj.StartIndex, SearchObj.PageSize, out numberOfObjectsPerPage);

        //    var jsonResult = Json(new
        //    {
        //        totalRecords = numberOfObjectsPerPage,
        //        aaData = produt
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}
        //public JsonResult Get_User_Contact_List(int CustomerID = 0)
        //{
        //    var Customer_Contact = _IMaster_Repository.Get_Customer_Contact_List(CustomerID);

        //    var jsonResult = Json(new
        //    {
        //        aaData = Customer_Contact
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}

        //public JsonResult SaveUpdateDeleteUser(gen_MstUser CM, Boolean IsDelete = false)
        //{
        //    int RetrunValue = 0;
        //    string xmlStr = string.Empty;

        //    XmlDocument xmlProduct = CommonFunction.ConvertToXml(CM);
        //    xmlStr += "<gen_MstUser>" + xmlProduct.DocumentElement.InnerXml + "</gen_MstUser>";

        //    //List<CustomerContact> GrdJobInwardDetails = CommonFunction.DataTableToList<CustomerContact>(CC);
        //    //GrdJobInwardDetails.Select(PV =>
        //    //{
        //    //    PV. = ji.Job_Id;
        //    //    return PV;
        //    //}).ToList();

        //    xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

        //    string OutIdentity = "0";
        //    RetrunValue = _IMaster_Repository.SaveUpdateDeleteUser(xmlStr, IsDelete, out OutIdentity);

        //    var jsonResult = Json(new
        //    {
        //        aaData = RetrunValue
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}


        //public JsonResult User_Contact_Delete(CustomerContact CustomerContact)
        //{
        //    var Response = _IMaster_Repository.Delete_Customer_Contact(CustomerContact.CustomerContactID);

        //    var jsonResult = Json(new
        //    {
        //        aaData = Response
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}

        //public JsonResult User_SaveUpdateDelete(int CustomerID = 0)
        //{
        //    var Customer_Contact = _IMaster_Repository.Get_Customer_Contact_List(CustomerID);

        //    var jsonResult = Json(new
        //    {
        //        aaData = Customer_Contact
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}

        //public JsonResult Get_User_Group_List()
        //{
        //    var produt = _IMaster_Repository.Get_User_Group_List();

        //    var jsonResult = Json(new
        //    {
        //        aaData = produt
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}

        //public JsonResult Get_User_Form_Permission(int GroupID=0)
        //{
        //    var produt = _IMaster_Repository.Get_User_Form_Permission(GroupID);

        //    var jsonResult = Json(new
        //    {
        //        aaData = produt
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}


        //public JsonResult User_Permission_SaveUpdateDelete(List<Get_User_Form_Permission_Result> UPL,gen_GroupMaster GM, Boolean IsDelete = false)
        //{
        //    int RetrunValue = 0;
        //    string xmlStr = string.Empty;

           
        //    XmlDocument xmlProductVariants = CommonFunction.ConvertToXml(GM);
        //    xmlStr += "<gen_GroupMaster>" + xmlProductVariants.DocumentElement.InnerXml + "</gen_GroupMaster>";
        //    if (UPL != null)
        //    {
        //        XmlDocument xmlProduct = CommonFunction.ConvertToXml(UPL);
        //        xmlStr += "<gen_GroupRights>" + xmlProduct.DocumentElement.InnerXml + "</gen_GroupRights>";
        //    }

        //    xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

        //    string OutIdentity = "0";
        //    RetrunValue = _IMaster_Repository.SaveUpdateDeleteUserPermission(xmlStr, IsDelete, out OutIdentity);

        //    var jsonResult = Json(new
        //    {
        //        aaData = RetrunValue
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}

        //public JsonResult check_User(string Name, int User_Id)
        //{
        //    var Response = _IMaster_Repository.check_User(Name, User_Id);

        //    var jsonResult = Json(new
        //    {
        //        aaData = Response
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}

        //public JsonResult check_User_Group(string Name, int Group_Id)
        //{
        //    var Response = _IMaster_Repository.check_User_Group(Name,Group_Id);

        //    var jsonResult = Json(new
        //    {
        //        aaData = Response
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}



    }
}