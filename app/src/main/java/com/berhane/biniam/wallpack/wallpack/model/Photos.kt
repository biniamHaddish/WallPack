package com.berhane.biniam.wallpack.wallpack.model

import android.os.Parcel
import android.os.Parcelable

import java.util.ArrayList

class Photos : Parcelable {

    /**
     * id : LBI7cgq3pbM
     * created_at : 2016-05-03T11:00:28-04:00
     * updated_at : 2016-07-10T11:00:01-05:00
     * width : 5245
     * height : 3497
     * color : #60544D
     * likes : 12
     * liked_by_user : false
     * description : A man drinking a coffee.
     * user : {"id":"pXhwzz1JtQU","username":"poorkane","name":"Gilbert Kane","portfolio_url":"https://theylooklikeeggsorsomething.com/","bio":"XO","location":"Way out there","total_likes":5,"total_photos":74,"total_collections":52,"instagram_username":"instantgrammer","twitter_username":"crew","profile_image":{"small":"https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=32&w=32","medium":"https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=64&w=64","large":"https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=128&w=128"},"links":{"self":"https://api.unsplash.com/users/poorkane","html":"https://unsplash.com/poorkane","photos":"https://api.unsplash.com/users/poorkane/photos","likes":"https://api.unsplash.com/users/poorkane/likes","portfolio":"https://api.unsplash.com/users/poorkane/portfolio"}}
     * current_user_collections : [{"id":206,"title":"Makers: Cat and Ben","published_at":"2016-01-12T18:16:09-05:00","updated_at":"2016-07-10T11:00:01-05:00","curated":false,"cover_photo":null,"user":null}]
     * urls : {"raw":"https://images.unsplash.com/face-springmorning.jpg","full":"https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg","regular":"https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=1080&fit=max","small":"https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=400&fit=max","thumb":"https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=200&fit=max"}
     * links : {"self":"https://api.unsplash.com/photos/LBI7cgq3pbM","html":"https://unsplash.com/photos/LBI7cgq3pbM","download":"https://unsplash.com/photos/LBI7cgq3pbM/download","download_location":"https://api.unsplash.com/photos/LBI7cgq3pbM/download"}
     */



    var id: String? = null
    var created_at: String? = null
    var updated_at: String? = null
    var width: Int = 0
    var height: Int = 0
    var color: String? = null
    var likes: Int = 0
    var isLiked_by_user: Boolean = false
    var description: String? = null
    var user: UserBean? = null
    var urls: UrlsBean? = null
    var links: LinksBeanX? = null
    var current_user_collections: List<CurrentUserCollectionsBean>? = null

    class UserBean protected constructor(`in`: Parcel) : Parcelable {
        /**
         * id : pXhwzz1JtQU
         * username : poorkane
         * name : Gilbert Kane
         * portfolio_url : https://theylooklikeeggsorsomething.com/
         * bio : XO
         * location : Way out there
         * total_likes : 5
         * total_photos : 74
         * total_collections : 52
         * instagram_username : instantgrammer
         * twitter_username : crew
         * profile_image : {"small":"https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=32&w=32","medium":"https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=64&w=64","large":"https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=128&w=128"}
         * links : {"self":"https://api.unsplash.com/users/poorkane","html":"https://unsplash.com/poorkane","photos":"https://api.unsplash.com/users/poorkane/photos","likes":"https://api.unsplash.com/users/poorkane/likes","portfolio":"https://api.unsplash.com/users/poorkane/portfolio"}
         */

        var id: String? = null
        var username: String? = null
        var name: String? = null
        var portfolio_url: String? = null
        var bio: String? = null
        var location: String? = null
        var total_likes: Int = 0
        var total_photos: Int = 0
        var total_collections: Int = 0
        var instagram_username: String? = null
        var twitter_username: String? = null
        var profile_image: ProfileImageBean? = null
        var links: LinksBean? = null

        init {
            id = `in`.readString()
            username = `in`.readString()
            name = `in`.readString()
            portfolio_url = `in`.readString()
            bio = `in`.readString()
            location = `in`.readString()
            total_likes = `in`.readInt()
            total_photos = `in`.readInt()
            total_collections = `in`.readInt()
            instagram_username = `in`.readString()
            twitter_username = `in`.readString()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeString(id)
            parcel.writeString(username)
            parcel.writeString(name)
            parcel.writeString(portfolio_url)
            parcel.writeString(bio)
            parcel.writeString(location)
            parcel.writeInt(total_likes)
            parcel.writeInt(total_photos)
            parcel.writeInt(total_collections)
            parcel.writeString(instagram_username)
            parcel.writeString(twitter_username)
        }

        class ProfileImageBean {
            /**
             * small : https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=32&w=32
             * medium : https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=64&w=64
             * large : https://images.unsplash.com/face-springmorning.jpg?q=80&fm=jpg&crop=faces&fit=crop&h=128&w=128
             */

            var small: String? = null
            var medium: String? = null
            var large: String? = null
        }

        class LinksBean {
            /**
             * self : https://api.unsplash.com/users/poorkane
             * html : https://unsplash.com/poorkane
             * photos : https://api.unsplash.com/users/poorkane/photos
             * likes : https://api.unsplash.com/users/poorkane/likes
             * portfolio : https://api.unsplash.com/users/poorkane/portfolio
             */

            var self: String? = null
            var html: String? = null
            var photos: String? = null
            var likes: String? = null
            var portfolio: String? = null
        }

        companion object {

            val CREATOR: Parcelable.Creator<UserBean> = object : Parcelable.Creator<UserBean> {
                override fun createFromParcel(`in`: Parcel): UserBean {
                    return UserBean(`in`)
                }

                override fun newArray(size: Int): Array<UserBean?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    class UrlsBean protected constructor(`in`: Parcel) : Parcelable {
        /**
         * raw : https://images.unsplash.com/face-springmorning.jpg
         * full : https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg
         * regular : https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=1080&fit=max
         * small : https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=400&fit=max
         * thumb : https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=200&fit=max
         */

        var raw: String? = null
        var full: String? = null
        var regular: String? = null
        var small: String? = null
        var thumb: String? = null

        init {
            raw = `in`.readString()
            full = `in`.readString()
            regular = `in`.readString()
            small = `in`.readString()
            thumb = `in`.readString()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeString(raw)
            parcel.writeString(full)
            parcel.writeString(regular)
            parcel.writeString(small)
            parcel.writeString(thumb)
        }

        companion object {

            val CREATOR: Parcelable.Creator<UrlsBean> = object : Parcelable.Creator<UrlsBean> {
                override fun createFromParcel(`in`: Parcel): UrlsBean {
                    return UrlsBean(`in`)
                }

                override fun newArray(size: Int): Array<UrlsBean?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    class LinksBeanX protected constructor(`in`: Parcel) : Parcelable {
        /**
         * self : https://api.unsplash.com/photos/LBI7cgq3pbM
         * html : https://unsplash.com/photos/LBI7cgq3pbM
         * download : https://unsplash.com/photos/LBI7cgq3pbM/download
         * download_location : https://api.unsplash.com/photos/LBI7cgq3pbM/download
         */

        var self: String? = null
        var html: String? = null
        var download: String? = null
        var download_location: String? = null

        init {
            self = `in`.readString()
            html = `in`.readString()
            download = `in`.readString()
            download_location = `in`.readString()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeString(self)
            parcel.writeString(html)
            parcel.writeString(download)
            parcel.writeString(download_location)
        }

        companion object {

            val CREATOR: Parcelable.Creator<LinksBeanX> = object : Parcelable.Creator<LinksBeanX> {
                override fun createFromParcel(`in`: Parcel): LinksBeanX {
                    return LinksBeanX(`in`)
                }

                override fun newArray(size: Int): Array<LinksBeanX?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    class CurrentUserCollectionsBean {
        /**
         * id : 206
         * title : Makers: Cat and Ben
         * published_at : 2016-01-12T18:16:09-05:00
         * updated_at : 2016-07-10T11:00:01-05:00
         * curated : false
         * cover_photo : null
         * user : null
         */

        var id: Int = 0
        var title: String? = null
        var published_at: String? = null
        var updated_at: String? = null
        var isCurated: Boolean = false
        var cover_photo: Any? = null
        var user: Any? = null
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.id)
        dest.writeString(this.created_at)
        dest.writeString(this.updated_at)
        dest.writeInt(this.width)
        dest.writeInt(this.height)
        dest.writeString(this.color)
        dest.writeInt(this.likes)
        dest.writeByte(if (this.isLiked_by_user) 1.toByte() else 0.toByte())
        dest.writeString(this.description)
        dest.writeParcelable(this.user, flags)
        dest.writeParcelable(this.urls, flags)
        dest.writeParcelable(this.links, flags)
        dest.writeList(this.current_user_collections)
    }

    constructor() {

    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readString()
        this.created_at = `in`.readString()
        this.updated_at = `in`.readString()
        this.width = `in`.readInt()
        this.height = `in`.readInt()
        this.color = `in`.readString()
        this.likes = `in`.readInt()
        this.isLiked_by_user = `in`.readByte().toInt() != 0
        this.description = `in`.readString()
        this.user = `in`.readParcelable(UserBean::class.java.classLoader)
        this.urls = `in`.readParcelable(UrlsBean::class.java.classLoader)
        this.links = `in`.readParcelable(LinksBeanX::class.java.classLoader)
        this.current_user_collections = ArrayList()
        `in`.readList(this.current_user_collections,  CurrentUserCollectionsBean::class.java.classLoader)
    }

    companion object {

        val CREATOR: Parcelable.Creator<Photos> = object : Parcelable.Creator<Photos> {
            override fun createFromParcel(source: Parcel): Photos {
                return Photos(source)
            }

            override fun newArray(size: Int): Array<Photos?> {
                return arrayOfNulls(size)
            }
        }
    }

}
