package com.gillongname.annotations

interface Clickable {
    fun <E : GencyclerModel> onItemClicked(element: E)
}