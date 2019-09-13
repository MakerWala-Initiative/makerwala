using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApp.Areas.Admin.Filters
{
    public class SearchFilter
    {
        public int Product_User_Id { get; set; }
        public string Search { get; set; }
        public string OrderBy { get; set; }
        public int StartIndex { get; set; }
        public int pageIndex { get; set; }
        public int PageSize { get; set; }
    }
}