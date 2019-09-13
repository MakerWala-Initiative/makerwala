package com.uvtech.makerwala.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.uvtech.makerwala.helpers.LogHelper
import com.uvtech.makerwala.models.DownloadListModel

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Database Version
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "MakerWalaDB"

        private const val TABLE_VIDEO_STORE = "VideoStore"

        private const val KEY_ID = "id"
        private const val KEY_USER_ID = "userId"
        private const val KEY_NAME = "name"
        private const val KEY_STATUS = "status"
        private const val KEY_VIDEO_URL = "videoUrl"
        private const val KEY_VIDEO_EXTENSION = "videoExtension"
        private const val KEY_IMAGE_URL = "imageUrl"
        private const val KEY_VIDEO_FILE_NAME = "videoFileName"
        private const val KEY_IMAGE_FILE_NAME = "imageFileName"

        private const val CREATE_TABLE_PROJECT = ("CREATE TABLE " + TABLE_VIDEO_STORE + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_USER_ID + " INTEGER, " + KEY_NAME + " TEXT, " +
                KEY_STATUS + " INTEGER, " + KEY_VIDEO_URL + " TEXT, " + KEY_VIDEO_EXTENSION + " TEXT, " + KEY_IMAGE_URL + " TEXT," +
                " " + KEY_VIDEO_FILE_NAME + " TEXT, " + KEY_IMAGE_FILE_NAME + " TEXT)");
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_PROJECT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_VIDEO_STORE")
        onCreate(db)
    }

    fun addVideo(downloadModel: DownloadListModel): Int {
        val db = writableDatabase

        val values = ContentValues()
        values.put(KEY_USER_ID, downloadModel.userId)
        values.put(KEY_NAME, downloadModel.name)
        values.put(KEY_STATUS, downloadModel.status)
        values.put(KEY_VIDEO_URL, downloadModel.videoUrl)
        values.put(KEY_VIDEO_EXTENSION, downloadModel.videoExtension)
        values.put(KEY_IMAGE_URL, downloadModel.imageUrl)
        values.put(KEY_VIDEO_FILE_NAME, downloadModel.videoFileName)
        values.put(KEY_IMAGE_FILE_NAME, downloadModel.imageFileName)

        val id = db.insert(TABLE_VIDEO_STORE, null, values)
        db.close()
        return id.toInt()
    }

    fun getAllVideos(): ArrayList<DownloadListModel> {
        val arrays = ArrayList<DownloadListModel>()

        val selectQuery = "SELECT * FROM $TABLE_VIDEO_STORE"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val localModel = DownloadListModel(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    if (cursor.getInt(3) == 0) 0 else 1
                )
                LogHelper.e(localModel.toString())
                arrays.add(localModel)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return arrays
    }

    fun getAllVideosUserId(id:Int): ArrayList<DownloadListModel> {
        val arrays = ArrayList<DownloadListModel>()

        val selectQuery = "SELECT * FROM $TABLE_VIDEO_STORE WHERE $KEY_USER_ID=$id"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val localModel = DownloadListModel(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    if (cursor.getInt(3) == 0) 0 else 1
                )
                LogHelper.e(localModel.toString())
                arrays.add(localModel)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return arrays
    }

    fun updateLocal(downloadListModel: DownloadListModel): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_USER_ID, downloadListModel.userId)
        values.put(KEY_NAME, downloadListModel.name)
        values.put(KEY_STATUS, downloadListModel.status)
        values.put(KEY_VIDEO_URL, downloadListModel.videoUrl)
        values.put(KEY_VIDEO_EXTENSION, downloadListModel.videoExtension)
        values.put(KEY_IMAGE_URL, downloadListModel.imageUrl)
        values.put(KEY_VIDEO_FILE_NAME, downloadListModel.videoFileName)
        values.put(KEY_IMAGE_FILE_NAME, downloadListModel.imageFileName)

        return db.update(
            TABLE_VIDEO_STORE, values, "$KEY_ID = ?",
            arrayOf(downloadListModel.id.toString())
        )
    }

    fun deleteVideo(i: Int) {
        val db = this.writableDatabase
        db.delete(
            TABLE_VIDEO_STORE, "$KEY_ID = ?",
            arrayOf(i.toString())
        )
        db.close()
    }

    /*///////------------------------------------ project list ----------------------------------------////////
    fun addProject(projectModel: ProjectModel): Int {
        val db = writableDatabase

        val values = ContentValues()
        values.put(KEY_NAME, projectModel.name)
        values.put(KEY_DESCRIPTION, projectModel.description)

        val id = db.insert(TABLE_PROJECTS, null, values)
        db.close()
        return id.toInt()
    }

    fun getAllProjects(): List<ProjectModel> {
        val projectModels = ArrayList<ProjectModel>()
        val selectQuery = "SELECT project.$KEY_ID,project.$KEY_NAME,project.$KEY_DESCRIPTION FROM $TABLE_PROJECTS AS project"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                Log.e("project ", "" + cursor.getInt(0) + "," + cursor.getString(1) + "," + cursor.getString(2))
                val projectModel = ProjectModel(
                        cursor.getString(1), cursor.getString(2))
                projectModel.id = cursor.getInt(0)
                projectModels.add(projectModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return projectModels
    }

    fun getProjectList(id: Int): ProjectModel? {
        val db = this.readableDatabase

        val cursor = db.query(TABLE_PROJECTS, arrayOf(KEY_ID, KEY_NAME, KEY_DESCRIPTION),
                "$KEY_ID=?",
                arrayOf(id.toString()), null, null, null, null)

        if (cursor != null) {
            cursor.moveToFirst()

            val projectModel = ProjectModel(
                    cursor.getString(1), cursor.getString(2))
            projectModel.id = cursor.getInt(0)
            return projectModel
        }
        cursor?.close()
        return null
    }

    fun updateProject(projectModel: ProjectModel): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_NAME, projectModel.name)
        values.put(KEY_DESCRIPTION, projectModel.description)

        return db.update(TABLE_PROJECTS, values, "$KEY_ID = ?",
                arrayOf(projectModel.id.toString()))
    }

    fun deleteProject(i: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_PROJECTS, "$KEY_ID = ?",
                arrayOf(i.toString()))
        db.close()
    }

    fun deleteAllProjects() {
        val db = this.writableDatabase
        db.delete(TABLE_PROJECTS, null, null)
        db.close()
    }

    fun getTotalExpenses(id: Int): Float {
        var expense: Float = 0F

        val selectQuery = "SELECT $KEY_EXPENSE_PRICE FROM $TABLE_EXPENSES WHERE $KEY_PROJECT_ID = $id"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                expense += cursor.getString(0).toFloat()
            } while (cursor.moveToNext())
        }
        cursor.close()

        return expense
    }

    ///////------------------------------------ member list ----------------------------------------////////
    fun addMember(projectMemberModel: ProjectMemberModel): Int {
        val db = writableDatabase

        val values = ContentValues()
        values.put(KEY_NAME, projectMemberModel.name)
        values.put(KEY_EMAIL_ID, projectMemberModel.email)
        values.put(KEY_PROJECT_ID, projectMemberModel.projectId)

        val id = db.insert(TABLE_MEMBERS, null, values)
        db.close()
        return id.toInt()
    }

    fun getMemberCount(id: Int): Int {
        var count = 0
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_MEMBERS WHERE $KEY_PROJECT_ID=$id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    fun getMemberName(id: Int): String {
        val selectQuery = "SELECT $KEY_NAME FROM $TABLE_MEMBERS WHERE $KEY_ID=$id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            return cursor.getString(0)
        }
        cursor.close()
        return ""
    }

    fun getAllProjectMembers(projectId: Int): List<ProjectMemberModel> {
        val projectModels = ArrayList<ProjectMemberModel>()
        val selectQuery = "SELECT member.$KEY_ID,member.$KEY_NAME,member.$KEY_EMAIL_ID,member.$KEY_PROJECT_ID " +
                "FROM $TABLE_MEMBERS AS member WHERE member.$KEY_PROJECT_ID = $projectId"
        Log.e("get " + projectId, "ss " + selectQuery)
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                Log.e("member ", "" + cursor.getInt(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getInt(3))
                val projectMemberModel = ProjectMemberModel(
                        cursor.getString(1), cursor.getString(2))
//                projectModel.id = cursor.getInt(0)
//                projectModels.add(projectModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return projectModels
    }

    fun getAllProjectExpenseMembers(projectId: Int): List<ExpenseMemberModel> {
        val expenseMemberModels = ArrayList<ExpenseMemberModel>()
        val selectQuery = "SELECT member.$KEY_ID,member.$KEY_NAME,member.$KEY_EMAIL_ID,member.$KEY_PROJECT_ID " +
                "FROM $TABLE_MEMBERS AS member WHERE member.$KEY_PROJECT_ID = $projectId"
//        Log.e("get " + projectId, "ss " + selectQuery)
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val expenseMemberModel = ExpenseMemberModel(
                        cursor.getString(1))
                expenseMemberModel.id = cursor.getInt(0)
                expenseMemberModels.add(expenseMemberModel)
//                Log.e("ex members ", "" + expenseMemberModel.id + "," + expenseMemberModel.name + "," + expenseMemberModel.price)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return expenseMemberModels
    }

    ///////------------------------------------ expense list ----------------------------------------////////
    fun addExpense(expenseModel: ExpenseModel): Int {
        val db = writableDatabase

        val values = ContentValues()
        values.put(KEY_DESCRIPTION, expenseModel.description)
        values.put(KEY_EXPENSE_PRICE, expenseModel.expensePrice)
        values.put(KEY_MEMBER_ID, expenseModel.memberId)
        values.put(KEY_PROJECT_ID, expenseModel.projectId)

        val id = db.insert(TABLE_EXPENSES, null, values)
        db.close()
        return id.toInt()
    }

    fun updateExpense(expenseModel: ExpenseModel): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_DESCRIPTION, expenseModel.description)
        values.put(KEY_EXPENSE_PRICE, expenseModel.expensePrice)
        values.put(KEY_MEMBER_ID, expenseModel.memberId)
        values.put(KEY_PROJECT_ID, expenseModel.projectId)

        db.update(TABLE_EXPENSES, values, "$KEY_ID = ?",
                arrayOf(expenseModel.id.toString()))
        db.close()
        return expenseModel.id
    }

    fun getExpenseDetail(id: Int): ExpenseModel {
        val selectQuery = "SELECT * FROM $TABLE_EXPENSES WHERE $KEY_ID=$id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            Log.e("expensede", "" + cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2))
            var expenseModel = ExpenseModel(cursor.getString(1), cursor.getString(2))
            expenseModel.id = cursor.getInt(0)
            expenseModel.memberId = cursor.getInt(3)
            expenseModel.projectId = cursor.getInt(4)
            return expenseModel
        }
        cursor.close()
        return ExpenseModel("", "")
    }

    fun getExpenseCount(projectId: Int): Float {
        var count: Float = 0F
        val selectQuery = "SELECT $KEY_EXPENSE_PRICE FROM $TABLE_EXPENSES WHERE $KEY_PROJECT_ID=$projectId"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                count += cursor.getString(0).toFloat()
            } while (cursor.moveToNext())
        }
        cursor.close()
        return count
    }

    fun getAllProjectExpenses(projectId: Int): List<ExpenseModel> {
        val expenseModels = ArrayList<ExpenseModel>()
        val selectQuery = "SELECT expense.$KEY_ID,expense.$KEY_DESCRIPTION,expense.$KEY_EXPENSE_PRICE,expense.$KEY_MEMBER_ID" +
                ",expense.$KEY_PROJECT_ID FROM $TABLE_EXPENSES AS expense WHERE expense.$KEY_PROJECT_ID = $projectId"
//        Log.e("get " + projectId, "ss " + selectQuery)
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
//                Log.e("expense ", "" + cursor.getInt(0) + "," + cursor.getString(1)
//                        + "," + cursor.getString(2) + "," + cursor.getInt(3))
                val expenseModel = ExpenseModel(
                        cursor.getString(1), cursor.getString(2))
                expenseModel.id = cursor.getInt(0)
                expenseModel.memberId = cursor.getInt(3)
                expenseModel.projectId = cursor.getInt(4)
                expenseModels.add(expenseModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return expenseModels
    }

    ///////------------------------------------ user expense list ----------------------------------------////////
    fun addUserExpense(userExpenseModel: UserExpenseModel): Int {
        val db = writableDatabase

        val values = ContentValues()
        values.put(KEY_EXPENSE_PRICE, userExpenseModel.expensePrice)
        values.put(KEY_EXPENSE_ID, userExpenseModel.expenseId)
        values.put(KEY_MEMBER_ID, userExpenseModel.memberId)
        values.put(KEY_PROJECT_ID, userExpenseModel.projectId)

        val id = db.insert(TABLE_USER_EXPENSES, null, values)
        db.close()
        return id.toInt()
    }

    fun updateUserExpense(userExpenseModel: UserExpenseModel): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(KEY_EXPENSE_PRICE, userExpenseModel.expensePrice)
        values.put(KEY_EXPENSE_ID, userExpenseModel.expenseId)
        values.put(KEY_MEMBER_ID, userExpenseModel.memberId)
        values.put(KEY_PROJECT_ID, userExpenseModel.projectId)

        db.update(TABLE_USER_EXPENSES, values, "$KEY_ID = ?",
                arrayOf(userExpenseModel.id.toString()))
        db.close()
        return userExpenseModel.id
    }

    fun getAllUserExpenses(expenseId: Int?): List<UserExpenseModel> {
        val userExpenseModels = ArrayList<UserExpenseModel>()
        val selectQuery = "SELECT * FROM $TABLE_USER_EXPENSES WHERE $KEY_EXPENSE_ID= $expenseId"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        Log.e("userexp " + expenseId, selectQuery)
        if (cursor.moveToFirst()) {
            do {
                Log.e("userexpes", "" + cursor.getInt(0) + "," + cursor.getString(1)
                        + "," + cursor.getInt(2)
                        + "," + cursor.getInt(3) + "," + cursor.getInt(4))
                val userExpenseModel = UserExpenseModel(cursor.getInt(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getString(1))
                userExpenseModel.id = cursor.getInt(0)
                userExpenseModels.add(userExpenseModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return userExpenseModels
    }

    fun getAllUserExpensesMemberId(memberId: Int?, projectId: Int?): Float {
        val selectQuery = "SELECT SUM($KEY_EXPENSE_PRICE) FROM $TABLE_USER_EXPENSES WHERE $KEY_MEMBER_ID= $memberId AND $KEY_PROJECT_ID= $projectId"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var value = 0F
        if (cursor.moveToFirst()) {
            value = cursor.getFloat(0)
        }
        cursor.close()
        return value
    }

    fun getAllUserExpensesProjectId(projectId: Int?): List<UserExpenseModel> {
        val userExpenseModels = ArrayList<UserExpenseModel>()
        val selectQuery = "SELECT * FROM $TABLE_USER_EXPENSES WHERE $KEY_PROJECT_ID= $projectId"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
//        Log.e("userexp " + projectId, selectQuery)
        if (cursor.moveToFirst()) {
            do {
//                Log.e("userexpes", "" + cursor.getInt(0) + "," + cursor.getString(1)
//                        + "," + cursor.getInt(2)
//                        + "," + cursor.getInt(3) + "," + cursor.getInt(4))
                val userExpenseModel = UserExpenseModel(cursor.getInt(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getString(1))
                userExpenseModel.id = cursor.getInt(0)
                userExpenseModels.add(userExpenseModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return userExpenseModels
    }

    fun getAllUserExpensesExpenseIdProjectId(expenseId: Int?, projectId: Int?): List<UserExpenseModel> {
        val userExpenseModels = ArrayList<UserExpenseModel>()
        val selectQuery = "SELECT * FROM $TABLE_USER_EXPENSES WHERE $KEY_EXPENSE_ID= $expenseId AND $KEY_PROJECT_ID= $projectId AND $KEY_EXPENSE_PRICE!='0.0'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        Log.e("userexp " + expenseId, selectQuery)
        if (cursor.moveToFirst()) {
            do {
                Log.e("userexpes", "" + cursor.getInt(0) + "," + cursor.getString(1)
                        + "," + cursor.getInt(2)
                        + "," + cursor.getInt(3) + "," + cursor.getInt(4))
                val userExpenseModel = UserExpenseModel(cursor.getInt(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getString(1))
                userExpenseModel.id = cursor.getInt(0)
                userExpenseModels.add(userExpenseModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return userExpenseModels
    }*/

}