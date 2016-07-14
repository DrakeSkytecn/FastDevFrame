package com.beyebe.fastdevframe.model

/**
 * Created by Kratos on 2016/1/19.
 */
class Home {

    var DefaultKeyword: String? = null
    var Picture: String? = null
    var AdsFocusList: Array<Ad>? = null

    class Ad {
        var Name: String? = null
        var Url: String? = null
        var Picture: String? = null
    }
}


