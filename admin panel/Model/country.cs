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
    using System.Collections.Generic;
    
    public partial class country
    {
        public country()
        {
            this.schools = new HashSet<school>();
            this.states = new HashSet<state>();
            this.teachers = new HashSet<teacher>();
        }
    
        public int countryid { get; set; }
        public string countryname { get; set; }
        public int createdby { get; set; }
        public System.DateTime createddate { get; set; }
        public Nullable<int> updatedby { get; set; }
        public Nullable<System.DateTime> updateddate { get; set; }
        public string remarks { get; set; }
        public Nullable<bool> isactive { get; set; }
    
        public virtual teacher teacher { get; set; }
        public virtual teacher teacher1 { get; set; }
        public virtual ICollection<school> schools { get; set; }
        public virtual ICollection<state> states { get; set; }
        public virtual ICollection<teacher> teachers { get; set; }
    }
}
