
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Data.Repository
{
    public interface IMaster_Repository : IDisposable
    {




        #region textbook

        List<Get_TextBook_List_Result> Get_Textbook_List(string sSearch = "");
        int SaveUpdateDelete_textbook(string xmlString, bool IsDelete, out string OutIdentity);
        #endregion textbook

        #region activitylogs

        List<Get_Activitylogs_List_Result> Get_activitylog_List(string sSearch, int StartIndex, int MaximumRows, out int numberOfObjectsPerPage);
        #endregion activitylogs

        #region UploadVideo

        List<Get_UploadVideo_List_Result> Get_UploadVideo_List(string sSearch, int StartIndex, int MaximumRows, out int numberOfObjectsPerPage);
        int SaveUpdateDelete_UploadVideo(string xmlString, bool IsDelete, out string OutIdentity);
        #endregion UploadVideo


        #region School
        List<Get_School_List_Result> Get_School_List(string sSearch, int user_Id, int StartIndex, int MaximumRows, out int numberOfObjectsPerPage);
        int SaveUpdateDelete_School(string xmlString, bool IsDelete, out string OutIdentity);
        #endregion School

        #region teacher
        List<Get_Teacher_List_Result> Get_Teacher_List(string sSearch, int user_Id, int StartIndex, int MaximumRows, out int numberOfObjectsPerPage);
        List<teacher> Get_Teachers_List();
        int SaveUpdateDelete_teacher(string xmlString, bool IsDelete, out string OutIdentity);

        #endregion teacher

        #region Subject
        List<subject> Get_subject_List();
        List<Get_Subject_List_Result> Get_subject_List(string classids = "", string sSearch = "");
        int SaveUpdateDelete_Subject(string xmlString, bool IsDelete, out string OutIdentity);
        #endregion Subject


        #region Class
        List<Get_Teacher_Subject_List_Result> Get_subjectforClass_List(string classid);
        List<Get_classlevels_List_Result> Get_Class_List(string sSearch = "");
        int SaveUpdateDelete_Class(string xmlString, bool IsDelete, out string OutIdentity);
        #endregion Class

        #region Master
        List<Get_Country_List_Result> Get_Country_List(string sSearch = "");
        //  int SaveUpdate_Delete_Country(country co, bool isDelete = false);
        int SaveUpdate_Delete_Country(string xmlString, bool IsDelete, out string OutIdentity);

        List<Get_State_List_Result> Get_State_List(int Country_Id);
        int SaveUpdateDelete_State(string xmlString, bool IsDelete, out string OutIdentity);

        List<Get_City_List_Result> Get_City_List(int Country_Id = 0, int State_Id = 0);
        int SaveUpdateDelete_City(string xmlString, bool IsDelete, out string OutIdentity);

        #endregion Master



        #region Block
        List<Get_Block_List_Result> Get_block_List(string sSearch = "");
        int SaveUpdateDelete_Block(string xmlString, bool IsDelete, out string OutIdentity);
        #endregion Block

        #region Cluster
        List<Get_clusters_List_Result> Get_cluster_List(string sSearch = "");
        int SaveUpdateDelete_cluster(string xmlString, bool IsDelete, out string OutIdentity);
        #endregion Cluster

        #region type
        List<Get_types_List_Result> Get_type_List(string sSearch = "");
        int SaveUpdateDelete_type(string xmlString, bool IsDelete, out string OutIdentity);
        #endregion type
    }
}
