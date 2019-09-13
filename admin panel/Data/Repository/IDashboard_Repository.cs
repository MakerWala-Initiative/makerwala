
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Data.Repository
{
    public interface IDashboard_Repository : IDisposable
    {

        List<Get_Dashboard_Chart_data_Result> Get_Dashboard_Chart_data(string chartType, string sSearch);

    }
}
