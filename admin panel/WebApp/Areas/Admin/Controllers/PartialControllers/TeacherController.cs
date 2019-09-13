using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Xml;
using WebApp.Areas.Admin.Filters;
namespace WebApp.Areas.Admin.Controllers
{
    public partial class TeacherController : AdminSessionController
    {

        //public JsonResult Get_Customer_List(SearchFilter SearchObj)
        //{
        //    int numberOfObjectsPerPage = 0;
        //    var sSearch = SearchObj.Search == null ? "" : SearchObj.Search;
        //    var produt = _IMaster_Repository.Get_Customer_List(sSearch, SearchObj.StartIndex, SearchObj.PageSize, out numberOfObjectsPerPage);

        //    var jsonResult = Json(new
        //    {
        //        totalRecords = numberOfObjectsPerPage,
        //        aaData = produt
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}
        //public JsonResult Get_Customer_Contact_List(int CustomerID = 0)
        //{
        //    var Customer_Contact = _IMaster_Repository.Get_Customer_Contact_List(CustomerID);

        //    var jsonResult = Json(new
        //    {
        //        aaData = Customer_Contact
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}

        //public JsonResult SaveUpdateDeleteCustomer(CustomerMaster CM, List<CustomerContact> CC, Boolean IsDelete = false)
        //{
        //    int RetrunValue = 0;
        //    string xmlStr = string.Empty;

        //    XmlDocument xmlProduct = CommonFunction.ConvertToXml(CM);
        //    xmlStr += "<CustomerMaster>" + xmlProduct.DocumentElement.InnerXml + "</CustomerMaster>";

        //    //List<CustomerContact> GrdJobInwardDetails = CommonFunction.DataTableToList<CustomerContact>(CC);
        //    //GrdJobInwardDetails.Select(PV =>
        //    //{
        //    //    PV. = ji.Job_Id;
        //    //    return PV;
        //    //}).ToList();

        //    if (CC != null)
        //    {
        //        XmlDocument xmlProductVariants = CommonFunction.ConvertToXml(CC);
        //        xmlStr += "<CustomerContact>" + xmlProductVariants.DocumentElement.InnerXml + "</CustomerContact>";
        //    }

        //    xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

        //    string OutIdentity = "0";
        //    RetrunValue = _IMaster_Repository.SaveUpdateDeleteCustomer(xmlStr, IsDelete, out OutIdentity);

        //    var jsonResult = Json(new
        //    {
        //        aaData = RetrunValue
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}


        //public JsonResult check_Customer(string Name, int CustomerID = 0)
        //{
        //    var Response = _IMaster_Repository.check_Customer(Name, CustomerID);

        //    var jsonResult = Json(new
        //    {
        //        aaData = Response
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}

        //public JsonResult Customer_Contact_Delete(CustomerContact CustomerContact)
        //{
        //    var Response = _IMaster_Repository.Delete_Customer_Contact(CustomerContact.CustomerContactID);

        //    var jsonResult = Json(new
        //    {
        //        aaData = Response
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}


        //public JsonResult Customer_SaveUpdateDelete(int CustomerID = 0)
        //{
        //    var Customer_Contact = _IMaster_Repository.Get_Customer_Contact_List(CustomerID);

        //    var jsonResult = Json(new
        //    {
        //        aaData = Customer_Contact
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}



    }
}