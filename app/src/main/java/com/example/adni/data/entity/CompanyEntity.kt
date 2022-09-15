package com.example.adni.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adni.domain.model.Company
import com.example.adni.domain.model.Coordinates

@Entity(tableName = "company")
data class CompanyEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "companyId")
        var companyId: Int,

        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "phone")
        var phone: String = "",

        @ColumnInfo(name = "address")
        var address: String = "",

        @ColumnInfo(name = "email")
        var email: String = "",

        @ColumnInfo(name = "logo")
        var logoPath: String = "",

        @ColumnInfo(name = "latitude")
        var latitude: String,

        @ColumnInfo(name = "longitude")
        var longitude: String
)

fun CompanyEntity.toCompanyModel(): Company {
    return Company(id = companyId, name = name, phone = phone,
            address = address, email = email, logoPath,
            coordinates = Coordinates(latitude.toDouble(), longitude.toDouble()))
}