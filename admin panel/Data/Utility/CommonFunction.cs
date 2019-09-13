using Data.Repository;
using Data.Services;
using Model;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.Entity.Core.EntityClient;
using System.Data.SqlClient;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Helpers;
using System.Xml;
using System.Xml.Serialization;

namespace Data.Utility
{
    public class CommonFunction
    {
        private dataEntities context;

        public CommonFunction(dataEntities context)
        {
            context.Configuration.ProxyCreationEnabled = false;
            this.context = context;
        }

        public static string GetConfigValue(string stConfigKeyName)
        {
            string stConfigValue = System.Configuration.ConfigurationManager.AppSettings[stConfigKeyName];
            return stConfigValue;
        }

        public static string CheckQueryString(string Query)
        {
            string[] SearchPara = GetConfigValue("QueryKeyward").Split(',');
            foreach (string keyward in SearchPara)
            {
                if (Query.Contains(keyward))
                    return "";
            }
            return Query;
        }


        #region QueryProcessiong
        public static DataTable ExcuteQuery(string Query)
        {
            DataTable results = new DataTable();
            string SqlConnectionString = ConfigurationManager.ConnectionStrings["dataEntities"].ConnectionString;
            var builder = new EntityConnectionStringBuilder(SqlConnectionString);
            SqlConnectionString = builder.ProviderConnectionString;
            using (SqlConnection con = new SqlConnection(SqlConnectionString))
            {
                using (SqlCommand cmd = new SqlCommand(Query, con))
                {
                    cmd.CommandText = Query;
                    cmd.Connection.Close();
                    cmd.Connection.Open();
                    results.Load(cmd.ExecuteReader());
                }
            }
            return results;
        }
        public static int ExcuteNonQuery(string Query)
        {
            int results = 0;
            string SqlConnectionString = ConfigurationManager.ConnectionStrings["dataEntities"].ConnectionString;
            var builder = new EntityConnectionStringBuilder(SqlConnectionString);
            SqlConnectionString = builder.ProviderConnectionString;
            using (SqlConnection con = new SqlConnection(SqlConnectionString))
            {
                using (SqlCommand cmd = new SqlCommand(Query, con))
                {
                    cmd.CommandText = Query;
                    cmd.Connection.Close();
                    cmd.Connection.Open();
                    results = cmd.ExecuteNonQuery();
                }
            }
            return results;
        }


        public static DataTable ExcuteQuerySPParam(string procName, string[,] paramArray)
        {
            SqlDataAdapter da = new SqlDataAdapter();
            DataTable results = new DataTable();
            string SqlConnectionString = ConfigurationManager.ConnectionStrings["dataEntities"].ConnectionString;
            var builder = new EntityConnectionStringBuilder(SqlConnectionString);
            SqlConnectionString = builder.ProviderConnectionString;
            //for (int r = 0; r < paramArray.GetLength(0); r++)
            //{
            //    Console.WriteLine(paramArray[r, 0] + " =>  " + paramArray[r, 1]);
            //}
            //if (paramArray.GetLength(1) != 2)
            //{
            //   // Console.WriteLine("paramArray has too few/too many columns. Exiting method...");
            //  //  System.Diagnostics.Debug.WriteLine("paramArray has too few/too many columns. Exiting method...");
            //}

            using (SqlConnection conn = new SqlConnection(SqlConnectionString))
            {
                SqlCommand cmd = new SqlCommand(procName, conn);
                cmd.CommandType = CommandType.StoredProcedure;
                for (int r = 0; r < paramArray.GetLength(0); r++)
                {
                    cmd.Parameters.Add(new SqlParameter(paramArray[r, 0], paramArray[r, 1]));
                }
                cmd.Connection.Open();
                // cmd.ExecuteNonQuery();
                da.SelectCommand = cmd;
                da.Fill(results);
                // System.Diagnostics.Debug.WriteLine("SQL Command executed successfully.");
            }
            return results;
        }
        public static DataSet ExcuteQueryInDataSet(string procName, string[,] paramArray)
        {
            SqlDataAdapter da = new SqlDataAdapter();
            DataSet results = new DataSet();
            string SqlConnectionString = ConfigurationManager.ConnectionStrings["dataEntities"].ConnectionString;
            var builder = new EntityConnectionStringBuilder(SqlConnectionString);
            SqlConnectionString = builder.ProviderConnectionString;
            using (SqlConnection conn = new SqlConnection(SqlConnectionString))
            {
                SqlCommand cmd = new SqlCommand(procName, conn);
                cmd.CommandType = CommandType.StoredProcedure;
                for (int r = 0; r < paramArray.GetLength(0); r++)
                {
                    cmd.Parameters.Add(new SqlParameter(paramArray[r, 0], paramArray[r, 1]));
                }
                cmd.Connection.Open();
                // cmd.ExecuteNonQuery();
                da.SelectCommand = cmd;
                da.Fill(results);
                // System.Diagnostics.Debug.WriteLine("SQL Command executed successfully.");
            }
            return results;
        }
        public static string GenerateCommandText(string storedProcedure, SqlParameter[] parameters)
        {
            string CommandText = "EXEC {0} {1}";
            string[] ParameterNames = new string[parameters.Length];

            for (int i = 0; i < parameters.Length; i++)
            {
                ParameterNames[i] = parameters[i].ParameterName;
            }

            return string.Format(CommandText, storedProcedure, string.Join(",", ParameterNames));
            //context.Database.ExecuteSqlCommand("exec MySchema.MyProc @customerId, @indicatorTypeId, @indicators, @startDate, @endDate", customerIdParam, typeIdParam, tableParameter, startDateParam, endDateParam);
        }

        #endregion #region QueryProcessiong



        #region ConvertTable
        public static DataTable ListToDataTable<T>(List<T> items)
        {
            DataTable dataTable = new DataTable(typeof(T).Name);

            //Get all the properties
            PropertyInfo[] Props = typeof(T).GetProperties(BindingFlags.Public | BindingFlags.Instance);
            foreach (PropertyInfo prop in Props)
            {
                //Setting column names as Property names
                dataTable.Columns.Add(prop.Name);
            }
            foreach (T item in items)
            {
                var values = new object[Props.Length];
                for (int i = 0; i < Props.Length; i++)
                {
                    //inserting property values to datatable rows
                    values[i] = Props[i].GetValue(item, null);
                }
                dataTable.Rows.Add(values);
            }
            //put a breakpoint here and check datatable
            return dataTable;
        }
        public static List<T> DataTableToList<T>(DataTable table) where T : class, new()
        {
            try
            {
                T tempT = new T();
                var tType = tempT.GetType();
                List<T> list = new List<T>();
                foreach (var row in table.Rows.Cast<DataRow>())
                {
                    T obj = new T();
                    foreach (var prop in obj.GetType().GetProperties())
                    {
                        var propertyInfo = tType.GetProperty(prop.Name);
                        if (table.Columns.Contains(prop.Name))
                        {
                            var rowValue = row[prop.Name];
                            var t = Nullable.GetUnderlyingType(propertyInfo.PropertyType) ?? propertyInfo.PropertyType;

                            try
                            {
                                object safeValue = (rowValue == null || DBNull.Value.Equals(rowValue)) ? null : Convert.ChangeType(rowValue, t);
                                propertyInfo.SetValue(obj, safeValue, null);

                            }
                            catch (Exception ex)
                            {//this write exception to my logger
                                // _logger.Error(ex.Message);
                            }
                        }
                        else
                        {
                            //object safeValue = 0.00;
                            //propertyInfo.SetValue(obj, safeValue, null);

                        }
                    }
                    list.Add(obj);
                }
                return list;
            }
            catch
            {
                return null;
            }
        }

        public static DataTable LINQToDataTable<T>(IEnumerable<T> linqList)
        {
            var dtReturn = new DataTable();
            PropertyInfo[] columnNameList = null;
            if (linqList == null || linqList.Count() == 0)
            {
                PropertyInfo[] t = typeof(T).GetProperties(BindingFlags.Public | BindingFlags.Instance);
                columnNameList = ((Type)t.GetType()).GetProperties();
                foreach (PropertyInfo columnName in t)
                {
                    Type columnType = columnName.PropertyType;
                    if ((columnType.IsGenericType) && (columnType.GetGenericTypeDefinition() == typeof(Nullable<>)))
                    {
                        columnType = columnType.GetGenericArguments()[0];
                    }
                    dtReturn.Columns.Add(new DataColumn(columnName.Name, columnType));
                }
                return dtReturn;
            }
            foreach (T t in linqList)
            {
                // Use reflection to get property names, to create table, Only first time, others will follow 
                if (columnNameList == null)
                {
                    columnNameList = ((Type)t.GetType()).GetProperties();
                    foreach (PropertyInfo columnName in columnNameList)
                    {
                        Type columnType = columnName.PropertyType;

                        if ((columnType.IsGenericType) && (columnType.GetGenericTypeDefinition() == typeof(Nullable<>)))
                        {
                            columnType = columnType.GetGenericArguments()[0];
                        }
                        dtReturn.Columns.Add(new DataColumn(columnName.Name, columnType));
                    }
                }
                DataRow dataRow = dtReturn.NewRow();
                foreach (PropertyInfo columnName in columnNameList)
                {
                    dataRow[columnName.Name] =
                        columnName.GetValue(t, null) == null ? DBNull.Value : columnName.GetValue(t, null);
                }
                dtReturn.Rows.Add(dataRow);
            }
            return dtReturn;

            #region Comment Code
            //DataTable dataTable = new DataTable();
            //PropertyInfo[] array = null;
            //bool flag = varlist == null;

            //DataTable result;
            //if (flag)
            //{
            //    result = dataTable;
            //}
            //else
            //{
            //    foreach (T current in varlist)
            //    {
            //        bool flag2 = array == null;
            //        if (flag2)
            //        {
            //            array = current.GetType().GetProperties();
            //            PropertyInfo[] array2 = array;
            //            for (int i = 0; i < array2.Length; i++)
            //            {
            //                PropertyInfo propertyInfo = array2[i];
            //                Type type = propertyInfo.PropertyType;
            //                bool flag3 = type.IsGenericType && type.GetGenericTypeDefinition() == typeof(Nullable<>);
            //                if (flag3)
            //                {
            //                    type = type.GetGenericArguments()[0];
            //                }
            //                dataTable.Columns.Add(new DataColumn(propertyInfo.Name, type));
            //            }
            //        }
            //        DataRow dataRow = dataTable.NewRow();
            //        PropertyInfo[] array3 = array;
            //        for (int j = 0; j < array3.Length; j++)
            //        {
            //            PropertyInfo propertyInfo2 = array3[j];
            //            dataRow[propertyInfo2.Name] = ((propertyInfo2.GetValue(current, null) == null) ? DBNull.Value : propertyInfo2.GetValue(current, null));
            //        }
            //        dataTable.Rows.Add(dataRow);
            //    }
            //    result = dataTable;
            //}
            //return result;
            #endregion Comment Code
        }

        public static XmlDocument ConvertToXml(Object list)
        {
            XmlDocument xmlDoc = new XmlDocument();
            XmlSerializer xmlSerializer = new XmlSerializer(list.GetType());
            using (MemoryStream xmlStream = new MemoryStream())
            {
                xmlSerializer.Serialize(xmlStream, list);
                xmlStream.Position = 0;
                xmlDoc.Load(xmlStream);
                return xmlDoc;
            }
        }

        public T Deserialize<T>(string input) where T : class
        {
            System.Xml.Serialization.XmlSerializer ser = new System.Xml.Serialization.XmlSerializer(typeof(T));

            using (StringReader sr = new StringReader(input))
            {
                return (T)ser.Deserialize(sr);
            }
        }

        #endregion ConvertTable



        public static string SaveStringByte(string byteString, String StorageName, string ExtraPath = "")
        {
            string FileName = "", dirPath = "";
            try
            {
                byte[] Imgbytes = System.Convert.FromBase64String(byteString.Replace("data:image/png;base64,", "").Replace("data:image/jpeg;base64,", ""));
                string StoragePath = HttpContext.Current.Server.MapPath(System.Configuration.ConfigurationManager.AppSettings[StorageName]) + ExtraPath;
                dirPath = System.Configuration.ConfigurationManager.AppSettings[StorageName] + ExtraPath;
                if (!System.IO.Directory.Exists(StoragePath))
                {
                    System.IO.Directory.CreateDirectory(StoragePath);
                }

                FileName = Guid.NewGuid().ToString("N") + "-" + DateTime.Now.ToString("dd-MM-yyyy-HH-mm-ss") + ".png";
                dirPath = dirPath + FileName;
                WebImage myWI = new WebImage(Imgbytes);
                myWI.Save(StoragePath + FileName, "png", false);
            }
            catch (Exception ex)
            {
                //ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());

                //activitylog al = new activitylog();
                //al.activitydate = DateTime.Now;
                //al.application = "web";
                //al.details = ex.InnerException.ToString();
                //al.type = "Exception";
                //al.userid = 0;
                //al.appversion = "0.00";
                //_ICommon_Repository.SaveUpdate_Delete_activitylogs(al);

                dirPath = "";
            }


            return dirPath;
        }




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
    }
}
