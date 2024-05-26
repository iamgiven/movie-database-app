package com.example.moviedatabase.models

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imgPoster: String = "",
    val releaseDate: String = ""
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        title = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        imgPoster = parcel.readString() ?: "",
        releaseDate = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(imgPoster)
        parcel.writeString(releaseDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}
