using Data.Repository;
using Data.Services;
using Data.Repository.Services;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Script.Serialization;
using Data.Utility;
using System.Xml;
using WebApp.Utility;
using System.Threading.Tasks;
using System.Threading;
using System.IO.Compression;
using System.IO;
using System.Web;

namespace WebApp.API
{
    public class HomeController : ApiController
    {
        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());


        [HttpPost]
        [Route("api/VideoList")]
        public ApiResponse VideoList([FromBody] apiparameters vl)
        {
            vl.pageindex = vl.pageindex - 1;
            String ResponseId = "failure", message = "Invalid Api Credentials", sSearch = "";
            int status = 0;
            VideoList vls = new VideoList();

            if (_ICommon_Repository.userapikey(vl.Key, vl.username, vl.password))
            {
                int numberOfObjectsPerPage = 0;

                if (vl.classid.Count > 0)
                {
                    sSearch += " vd.classlevelid in(" + string.Join(",", vl.classid) + ")";
                }
                if (vl.subjectid.Count > 0)
                {
                    if (vl.classid.Count > 0)
                    {
                        sSearch = " and ( " + sSearch + " or vd.subjectid in(" + string.Join(",", vl.subjectid) + "))";
                    }
                    else
                    {
                        sSearch = " and ( vd.subjectid in(" + string.Join(",", vl.subjectid) + "))";
                    }
                }

                var UploadVideoList = _IMaster_Repository.Get_UploadVideo_List(sSearch, vl.pageindex, 0, out numberOfObjectsPerPage).ToList();


                vls.VideoLists = UploadVideoList;
                vls.totalrecords = numberOfObjectsPerPage;


                if (UploadVideoList != null)
                {
                    ResponseId = "success";
                    message = "Video List Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure";
                    message = "Video List Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, vls, ResponseId);
        }


        [HttpPost]
        [Route("api/VideoDetail")]
        public ApiResponse VideoDetail([FromBody]apiparameters vds)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials", sSearch = "";
            int status = 0;
            VideoDetails Vd = new VideoDetails();

            if (_ICommon_Repository.userapikey(vds.Key, vds.username, vds.password))
            {
                int numberOfObjectsPerPage = 0;

                if (vds.videoid > 0)
                {
                    sSearch += " and vd.videoid=" + vds.videoid;
                }

                var VideoDetails = _IMaster_Repository.Get_UploadVideo_List(sSearch, 0, 0, out numberOfObjectsPerPage).FirstOrDefault();
                sSearch = " and vd.videoid<>" + vds.videoid;
                if (VideoDetails.classlevelid > 0)
                {
                    sSearch += " and vd.classlevelid=" + VideoDetails.classlevelid;
                }
                if (VideoDetails.subjectid > 0)
                {
                    sSearch += " and vd.subjectid=" + VideoDetails.subjectid;
                }
                var RelativeVideoList = _IMaster_Repository.Get_UploadVideo_List(sSearch, 0, 0, out numberOfObjectsPerPage).ToList();



                if (VideoDetails.videoid > 0)
                {
                    sSearch = " and ts.refid=" + VideoDetails.videoid + " and ts.transtypeid=1 and ts.reftypeid=1 ";
                }
                var VideoComments = _ICommon_Repository.gettransactionlog(sSearch).ToList();

                Vd.Details = VideoDetails;
                Vd.RelativeVideoList = RelativeVideoList;
                Vd.transactionlog = VideoComments;

                if (VideoDetails != null)
                {
                    ResponseId = "success";
                    message = "Video Detail Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure";
                    message = "Video Detail Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, Vd, ResponseId);
        }

        [HttpPost]
        [Route("api/relatedvideo")]
        public ApiResponse RelatedVideo([FromBody]apiparameters vds)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials", sSearch = "";
            int status = 0;

            int numberOfObjectsPerPage = 0;

            List<Get_UploadVideo_List_Result> RelativeVideoList = new List<Get_UploadVideo_List_Result>();
            if (_ICommon_Repository.userapikey(vds.Key, vds.username, vds.password))
            {
                if (vds.classid.Count > 0)
                {
                    sSearch += " and vd.classlevelid in(" + string.Join(",", vds.classid) + ")";
                }
                if (vds.subjectid.Count > 0)
                {
                    sSearch += " and vd.subjectid in(" + string.Join(",", vds.subjectid) + ")"; ;
                }

                RelativeVideoList = _IMaster_Repository.Get_UploadVideo_List(sSearch, vds.pageindex, 0, out numberOfObjectsPerPage).ToList();

                if (RelativeVideoList != null)
                {
                    ResponseId = "success";
                    message = "Related video list Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure";
                    message = "Related video list Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, RelativeVideoList, ResponseId);
        }

        [HttpPost]
        [Route("api/Registration")]
        public ApiResponse Registration([FromBody]Get_Teacher_List_Result tch)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;

            if (_ICommon_Repository.userapikey(tch.Key))
            {
                tch.classid = string.Join(",", tch.classids);
                tch.subjectid = string.Join(",", tch.subjectids);

                tch.createddate = DateTime.Now;
                tch.updateddate = DateTime.Now;

                tch.imgprofile = CommonFunction.SaveStringByte(tch.imgprofile, "ProfileImage", "");

                int RetrunValue = 0;
                string xmlStr = string.Empty;

                XmlDocument xmlProduct = CommonFunction.ConvertToXml(tch);
                xmlStr += "<teacher>" + xmlProduct.DocumentElement.InnerXml + "</teacher>";
                xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

                string OutIdentity = "0";
                RetrunValue = _IMaster_Repository.SaveUpdateDelete_teacher(xmlStr, false, out OutIdentity);


                if (RetrunValue == 1)
                {
                    ResponseId = "success";
                    message = "Register Successfully";
                    status = 1;
                }
                else if (RetrunValue == 2)
                {
                    ResponseId = "failure";
                    message = "Already exist Email or mobile no";
                    status = 0;
                }
                else
                {
                    ResponseId = "failure";
                    message = "Register Not Successfully";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, null, ResponseId);
        }

        [HttpPost]
        [Route("api/Login")]
        public ApiResponse Login([FromBody]apiparameters ln)
        {

            if (string.IsNullOrWhiteSpace(ln.username))
            {
                throw new Exception("username is missing");
            }

            teacher tch = new teacher();
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            login login = new login();


            if (_ICommon_Repository.userapikey(ln.Key))
            {
                if (ln.type == 1 || ln.type == 3)
                {
                    tch = _ICommon_Repository.Login_User(ln.username);
                    if (tch != null)
                    {
                        ln.password = tch.password;
                    }
                }
                login.logindetail = _ICommon_Repository.Login_User(ln.username, ln.password);
                if (login.logindetail != null)
                {
                    login.teacherclass = _ICommon_Repository.gettecherclass(login.logindetail.teacherid);
                    login.teachersubject = _ICommon_Repository.gettechersubject(login.logindetail.teacherid);

                    if (login.logindetail.imgprofile != "")
                        login.logindetail.imgprofile = CommonMethods.GetBase64StringForImage(HttpContext.Current.Server.MapPath(login.logindetail.imgprofile));

                    if (login.logindetail.isactive == true)
                    {
                        if (ln.type == 3)
                        {
                            string smstext = login.logindetail.otpcode + " is your MAKERWALA verification code";
                            CommonMethods.Sendsms(login.logindetail.teachermobile, smstext);

                            smslog sl = new smslog();
                            sl.smsid = 1;
                            sl.refid = login.logindetail.teacherid;
                            sl.reftypeid = 2;
                            sl.smsmobileno = login.logindetail.teachermobile;
                            sl.smstemplate = smstext;
                            sl.companyid = 1;
                            sl.branchid = 1;
                            sl.createdby = login.logindetail.teacherfirstname;
                            sl.createddate = DateTime.Now;

                            _ICommon_Repository.SaveUpdate_Delete_smslogs(sl);
                        }
                        ResponseId = "success";
                        message = "Log in Successfully";
                        status = 1;
                    }
                    else
                    {
                        message = "In Active User Credentials";
                        ResponseId = "failure";
                        status = 0;
                    }
                }
                else
                {
                    message = "Not match Credentials";
                    ResponseId = "failure";
                    status = -1;
                }
            }
            return new ApiResponse(status, message, login, ResponseId);

        }

        [HttpPost]
        [Route("api/changepassword")]
        public ApiResponse changepassword([FromBody]apiparameters cp)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            if (_ICommon_Repository.userapikey(cp.Key, cp.username, cp.password))
            {
                var statuspasswrod = _ICommon_Repository.SaveUpdate_Password(cp.userid, cp.currentpassword, cp.newpassword);

                if (statuspasswrod == 1)
                {
                    ResponseId = "success";
                    message = "Change Password Successfully";
                    status = 1;
                }
                else
                {
                    message = "Password not change Successfully";
                    ResponseId = "failure";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, null, ResponseId);
        }


        [HttpPost]
        [Route("api/resetpassword")]
        public ApiResponse Resetpassword([FromBody]apiparameters rp)
        {

            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            if (_ICommon_Repository.userapikey(rp.Key, rp.username, rp.password))
            {
                var statuspasswrod = _ICommon_Repository.Get_ResetPassword_Details(rp.username);
                if (statuspasswrod != null)
                {
                    status = MailTemplates.ResetMail(statuspasswrod);
                }
                if (status == 1)
                {
                    ResponseId = "success";
                    message = "Please check your email";
                    status = 1;
                }
                else
                {
                    message = "Please try again For mail failure";
                    ResponseId = "failure";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, null, ResponseId);
        }

        [HttpPost]
        [Route("api/Country")]
        public ApiResponse Country([FromBody]apiparameters c)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            List<Get_Country_List_Result> CountryList = new List<Get_Country_List_Result>();
            if (_ICommon_Repository.userapikey(c.Key, c.username, c.password))
            {
                CountryList = _IMaster_Repository.Get_Country_List("");
                if (CountryList != null)
                {
                    ResponseId = "success";
                    message = "Country List Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure";
                    message = "Country List Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, CountryList, ResponseId);
        }

        [HttpPost]
        [Route("api/State")]
        public ApiResponse State([FromBody]apiparameters s)
        {
            String ResponseId = "failure", message = "Invalid Api Key";
            int status = 0;
            List<Get_State_List_Result> StateList = new List<Get_State_List_Result>();
            if (_ICommon_Repository.userapikey(s.Key, s.username, s.password))
            {
                StateList = _IMaster_Repository.Get_State_List(s.countryid);
                if (StateList != null)
                {
                    ResponseId = "success";
                    message = "State List Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure";
                    message = "State List Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, StateList, ResponseId);
        }

        [HttpPost]
        [Route("api/City")]
        public ApiResponse City([FromBody]apiparameters c)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            List<Get_City_List_Result> cityList = new List<Get_City_List_Result>();
            if (_ICommon_Repository.userapikey(c.Key, c.username, c.password))
            {
                cityList = _IMaster_Repository.Get_City_List(c.countryid, c.stateid);
                if (cityList != null)
                {
                    ResponseId = "success";
                    message = "City List Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure"; ;
                    message = "City List Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, cityList, ResponseId);
        }

        [HttpPost]
        [Route("api/school")]
        public ApiResponse school([FromBody]apiparameters c)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            List<Get_School_List_Result> SchoolList = new List<Get_School_List_Result>();
            if (_ICommon_Repository.userapikey(c.Key, c.username, c.password))
            {
                int numberOfObjectsPerPage = 0;
                SchoolList = _IMaster_Repository.Get_School_List("", 0, 0, 0, out numberOfObjectsPerPage).ToList();

                if (SchoolList != null)
                {
                    ResponseId = "success";
                    message = "School List Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure"; ;
                    message = "School List Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, SchoolList, ResponseId);
        }

        [HttpPost]
        [Route("api/class")]
        public ApiResponse classlevel([FromBody]apiparameters c)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            List<Get_classlevels_List_Result> classList = new List<Get_classlevels_List_Result>();
            if (_ICommon_Repository.userapikey(c.Key, c.username, c.password))
            {
                classList = _IMaster_Repository.Get_Class_List("").ToList();
                if (classList != null)
                {
                    ResponseId = "success";
                    message = "class List Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure"; ;
                    message = "class List Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, classList, ResponseId);
        }

        [HttpPost]
        [Route("api/subject")]
        public ApiResponse subject([FromBody]apiparameters c)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials", stclassids = "";
            int status = 0;
            List<Get_Subject_List_Result> subjectList = new List<Get_Subject_List_Result>();

            if (_ICommon_Repository.userapikey(c.Key, c.username, c.password))
            {
                if (c.classid.Count > 0)
                {
                    stclassids += " and Classid in (" + string.Join(",", c.classid) + ")";
                }

                subjectList = _IMaster_Repository.Get_subject_List(stclassids, "").ToList();

                if (subjectList != null)
                {
                    ResponseId = "success";
                    message = "subject List Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure"; ;
                    message = "subject List Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, subjectList, ResponseId);
        }

        [HttpPost]
        [Route("api/block")]
        public ApiResponse block([FromBody]apiparameters c)
        {

            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            List<Get_Block_List_Result> blockList = new List<Get_Block_List_Result>();

            if (_ICommon_Repository.userapikey(c.Key, c.username, c.password))
            {
                blockList = _IMaster_Repository.Get_block_List().ToList();

                if (blockList != null)
                {
                    ResponseId = "success";
                    message = "block List Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure"; ;
                    message = "block List Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, blockList, ResponseId);
        }

        [HttpPost]
        [Route("api/type")]
        public ApiResponse type([FromBody]apiparameters c)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            List<Get_types_List_Result> typeList = new List<Get_types_List_Result>();
            if (_ICommon_Repository.userapikey(c.Key, c.username, c.password))
            {
                typeList = _IMaster_Repository.Get_type_List("").ToList();

                if (typeList != null)
                {
                    ResponseId = "success";
                    message = "type List Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure"; ;
                    message = "type List Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, typeList, ResponseId);
        }
        [HttpPost]
        [Route("api/cluster")]
        public ApiResponse cluster([FromBody]apiparameters c)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            List<Get_clusters_List_Result> clusterList = new List<Get_clusters_List_Result>();
            if (_ICommon_Repository.userapikey(c.Key, c.username, c.password))
            {
                clusterList = _IMaster_Repository.Get_cluster_List("").ToList();

                if (clusterList != null)
                {
                    ResponseId = "success";
                    message = "cluster List Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure"; ;
                    message = "cluster List Not Fetched";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, clusterList, ResponseId);
        }

        [HttpPost]
        [Route("api/transactionlog")]
        public ApiResponse transactionlog([FromBody]transactionlog tl)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;
            Get_Transactions_List_Result RetrunValue = new Get_Transactions_List_Result();
            if (_ICommon_Repository.userapikey(tl.Key, tl.username, tl.password))
            {
                tl.transdatetime = DateTime.Now;
                tl.createddate = DateTime.Now;

                string xmlStr = string.Empty;

                XmlDocument xmlProduct = CommonFunction.ConvertToXml(tl);
                xmlStr += "<transactionlog>" + xmlProduct.DocumentElement.InnerXml + "</transactionlog>";
                xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

                string OutIdentity = "0";
                RetrunValue = _ICommon_Repository.SaveUpdate_Delete_transactionlog(xmlStr, tl.isrecorddelete, out OutIdentity);

                if (RetrunValue != null)
                {
                    ResponseId = "success";
                    message = "transactionlog Successfully";
                    status = 1;
                }
                else if (OutIdentity == "3")
                {
                    ResponseId = "success";
                    message = "transactionlog Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure";
                    message = "transactionlog Not Successfully";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, RetrunValue, ResponseId);
        }

        [HttpPost]
        [Route("api/activitylogs")]
        public ApiResponse activitylogs([FromBody]activitylog tl)
        {
            String ResponseId = "failure", message = "Invalid Api Credentials";
            int status = 0;

            if (_ICommon_Repository.userapikey(tl.Key, tl.username, tl.password))
            {
                tl.activitydate = DateTime.Now;

                int RetrunValue = _ICommon_Repository.SaveUpdate_Delete_activitylogs(tl);

                if (RetrunValue == 1)
                {
                    ResponseId = "success";
                    message = "activitylogs Successfully";
                    status = 1;
                }
                else
                {
                    ResponseId = "failure";
                    message = "activitylogs Not Successfully";
                    status = 0;
                }
            }
            return new ApiResponse(status, message, null, ResponseId);
        }
    }
}