package com.gillongname.corelisteners

import goldzweigapps.com.annotations.annotations.GencyclerModel

interface OnItemClickedListener<T: GencyclerModel> :
    OnItemChangedListener<T> {
	fun onItemClicked(item: T, position: Int)
}