//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Model
{
    using System;
    
    public partial class Get_State_List_Result
    {
        public int stateid { get; set; }
        public string statename { get; set; }
        public int countryid { get; set; }
        public string countryname { get; set; }
        public int createdby { get; set; }
        public System.DateTime createddate { get; set; }
        public Nullable<int> updatedby { get; set; }
        public Nullable<System.DateTime> updateddate { get; set; }
        public string remakrs { get; set; }
        public Nullable<bool> isactive { get; set; }
    }
}
