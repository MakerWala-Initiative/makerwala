using Data.Repository;
using Data.Utility;
using Model;
using System;
using System.Collections.Generic;
using System.Data.Entity.Core.Objects;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Data.Repository.Services
{
    public class Master_Repository : IMaster_Repository
    {
        private dataEntities context;

        public Master_Repository(dataEntities context)
        {

            context.Configuration.ProxyCreationEnabled = false;
            this.context = context;
        }
        //#region Customer


        ////public int Delete_Customer_Contact(int customerContactId = 0)
        ////{
        ////    int returnValue = 0;
        ////    var CustomerContacts = context.CustomerContacts.Where(x => x.CustomerContactID == customerContactId).FirstOrDefault();
        ////    if (CustomerContacts != null)
        ////    {
        ////        string Query = "DELETE dbo.CustomerContacts WHERE CustomerContactID=" + CustomerContacts.CustomerContactID + "";
        ////        CommonFunction.ExcuteNonQuery(Query);
        ////        returnValue = 1;
        ////    }
        ////    return returnValue;
        ////}


        //#endregion Customer




        #region textbook

        public List<Get_TextBook_List_Result> Get_Textbook_List(string sSearch = "")
        {

            sSearch = CommonFunction.CheckQueryString(sSearch);
            return context.Get_TextBook_List(sSearch).ToList();
        }


        public int SaveUpdateDelete_textbook(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.TextBook_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }

        #endregion textbook

        #region activitylogs

        public List<Get_Activitylogs_List_Result> Get_activitylog_List(string sSearch, int StartIndex, int MaximumRows, out int numberOfObjectsPerPage)
        {
            sSearch = CommonFunction.CheckQueryString(sSearch);
            var Out = new ObjectParameter("TotalRecords", typeof(int));
            var list = context.Get_Activitylogs_List(sSearch, StartIndex, MaximumRows, Out).ToList();
            numberOfObjectsPerPage = Convert.ToInt32(Out.Value);
            return list;
        }
        #endregion activitylogs
        #region UploadVideo

        public List<Get_UploadVideo_List_Result> Get_UploadVideo_List(string sSearch, int StartIndex, int MaximumRows, out int numberOfObjectsPerPage)
        {
            sSearch = CommonFunction.CheckQueryString(sSearch);
            var Out = new ObjectParameter("TotalRecords", typeof(int));
            var list = context.Get_UploadVideo_List(sSearch, StartIndex, MaximumRows, Out).ToList();
            numberOfObjectsPerPage = Convert.ToInt32(Out.Value);
            return list;
        }

        public int SaveUpdateDelete_UploadVideo(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.Upload_video_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }

        #endregion UploadVideo

        #region teacher

        public List<Get_Teacher_List_Result> Get_Teacher_List(string sSearch, int user_Id, int StartIndex, int MaximumRows, out int numberOfObjectsPerPage)
        {
            sSearch = CommonFunction.CheckQueryString(sSearch);
            var Out = new ObjectParameter("TotalRecords", typeof(int));
            var list = context.Get_Teacher_List(sSearch, user_Id, StartIndex, MaximumRows, Out).ToList();
            numberOfObjectsPerPage = Convert.ToInt32(Out.Value);
            return list;
        }
        public List<teacher> Get_Teachers_List()
        {
            return context.teachers.Where(x => x.isactive == true).ToList();
        }
        public int SaveUpdateDelete_teacher(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.Teacher_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }

        #endregion teacher

        #region School
        public List<Get_School_List_Result> Get_School_List(string sSearch, int user_Id, int StartIndex, int MaximumRows, out int numberOfObjectsPerPage)
        {
            sSearch = CommonFunction.CheckQueryString(sSearch);
            var Out = new ObjectParameter("TotalRecords", typeof(int));
            var list = context.Get_School_List(sSearch, user_Id, StartIndex, MaximumRows, Out).ToList();
            numberOfObjectsPerPage = Convert.ToInt32(Out.Value);
            return list;
        }


        public int SaveUpdateDelete_School(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.School_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }
        #endregion School

        #region Master


        //public List<Get_User_List_Result> Get_User_List(string sSearch, int user_Id, int StartIndex, int MaximumRows, out int numberOfObjectsPerPage)
        //{
        //    var Out = new ObjectParameter("TotalRecords", typeof(int));
        //    var list = context.Get_User_List(sSearch, user_Id, StartIndex, MaximumRows, Out).ToList();
        //    numberOfObjectsPerPage = Convert.ToInt32(Out.Value);
        //    return list;
        //}

        public List<Get_Country_List_Result> Get_Country_List(string sSearch = "")
        {
            sSearch = CommonFunction.CheckQueryString(sSearch);
            return context.Get_Country_List(sSearch).ToList();
        }
        public int SaveUpdate_Delete_Country(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.country_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }

        //public int SaveUpdate_Delete_Country(country co, bool isDelete = false)
        //{
        //    int ResponseId = 0;
        //    var existCountry = context.countries.Where(x => x.countryid == co.countryid).FirstOrDefault();
        //    if (existCountry == null)
        //    {
        //        context.countries.Add(co);
        //        int i = context.SaveChanges();
        //        ResponseId = 1;
        //    }
        //    else if (existCountry != null && isDelete)
        //    {
        //        context.countries.Remove(existCountry);
        //        context.SaveChanges();
        //        ResponseId = 3;
        //    }
        //    else
        //    {
        //        co.createddate = existCountry.createddate;
        //        co.createdby = existCountry.createdby;
        //        //existCountry.isactive = co.isactive;
        //        context.Entry(existCountry).CurrentValues.SetValues(co);
        //        context.SaveChanges();
        //        ResponseId = 2;
        //    }
        //    return ResponseId;
        //}


        public List<Get_State_List_Result> Get_State_List(int Country_Id)
        {
            return context.Get_State_List(Country_Id).ToList();

        }
        public int SaveUpdateDelete_State(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.State_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }

        public List<Get_City_List_Result> Get_City_List(int Country_Id = 0, int State_Id = 0)
        {
            return context.Get_City_List(Country_Id, State_Id).ToList();
        }

        public int SaveUpdateDelete_City(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.City_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }



        #endregion Master

        #region Subject
        public List<Get_Teacher_Subject_List_Result> Get_subjectforClass_List(string classid)
        {
            return context.Get_Teacher_Subject_List(classid).ToList();
        }

        public List<subject> Get_subject_List()
        {
            return context.subjects.ToList();
        }

        public List<Get_Subject_List_Result> Get_subject_List(string classids = "", string sSearch = "")
        {
            sSearch = CommonFunction.CheckQueryString(sSearch);
            return context.Get_Subject_List(classids, sSearch).ToList();
        }
        public int SaveUpdateDelete_Subject(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.Subject_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }

        #endregion Subject

        #region Class
        public List<Get_classlevels_List_Result> Get_Class_List(string sSearch = "")
        {
            sSearch = CommonFunction.CheckQueryString(sSearch);
            return context.Get_classlevels_List(sSearch).ToList();
        }


        public int SaveUpdateDelete_Class(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.Class_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }


        #endregion Class

        #region Block
        public List<Get_Block_List_Result> Get_block_List(string sSearch = "")
        {
            sSearch = CommonFunction.CheckQueryString(sSearch);
            return context.Get_Block_List(sSearch).ToList();
        }


        public int SaveUpdateDelete_Block(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.Block_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }

        #endregion Block

        #region Cluster


        public List<Get_clusters_List_Result> Get_cluster_List(string sSearch = "")
        {
            sSearch = CommonFunction.CheckQueryString(sSearch);
            return context.Get_clusters_List(sSearch).ToList();
        }


        public int SaveUpdateDelete_cluster(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.Cluster_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }

        #endregion Cluster

        #region type

        public List<Get_types_List_Result> Get_type_List(string sSearch = "")
        {
            sSearch = CommonFunction.CheckQueryString(sSearch);
            return context.Get_types_List(sSearch).ToList();
        }
        public int SaveUpdateDelete_type(string xmlString, bool IsDelete, out string OutIdentity)
        {
            var Out_Identity = new ObjectParameter("OutIdentity", typeof(string));
            int Return_Value = context.Type_SaveUpdateDelete(xmlString, IsDelete, Out_Identity).FirstOrDefault().Value;
            OutIdentity = Out_Identity.Value.ToString();
            return Return_Value;
        }

        #endregion type


        private bool disposed = false;

        protected virtual void Dispose(bool disposing)
        {
            if (!this.disposed)
            {
                if (disposing)
                {
                    context.Dispose();
                }
            }
            this.disposed = true;
        }

        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        public string ContainsAny { get; set; }
    }
}
