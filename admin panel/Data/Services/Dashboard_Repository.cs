
using Data.Repository;
using Data.Utility;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Data.Repository
{
    public class Dashboard_Repository : IDashboard_Repository
    {

        private dataEntities context;

        public Dashboard_Repository(dataEntities context)
        {
            context.Configuration.ProxyCreationEnabled = false;
            this.context = context;
        }





        public List<Get_Dashboard_Chart_data_Result> Get_Dashboard_Chart_data(string chartType, string sSearch)
        {
            return context.Get_Dashboard_Chart_data(chartType,sSearch).ToList();
        }







        private bool disposed = false;
        private dataEntities dataEntities;

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
