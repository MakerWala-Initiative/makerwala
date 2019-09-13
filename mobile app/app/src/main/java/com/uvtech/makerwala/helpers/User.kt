package com.uvtech.makerwala.helpers

data class User(
    var id: Int = 0, var blockId: Int = 0, var blockName: String = "", var teacherMobile: String = "",
    var teacherEmailAddress: String = "", var teacherAddress: String = "", var password: String = "",
    var countryId: Int = 0, var countryName: String = "",
    var stateId: Int = 0, var stateName: String = "", var cityId: Int = 0, var cityName: String = "",
    var userRoleId: Int = 0, var schoolId: Int = 0, var schoolName: String = "",
    var teacherFirstName: String = "", var teacherLastName: String = "",
    var imgProfile: String = "",
    var classIds: ArrayList<Int> = ArrayList(), var classNames: ArrayList<String> = ArrayList(),
    var subjectIds: ArrayList<Int> = ArrayList(), var subjectNames: ArrayList<String> = ArrayList()
) {

}
