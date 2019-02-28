package goldzweigapps.com.compiler.consts

import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.GencyclerModel

object KDocs {

    val ADAPTER_CLASS = """
		RecyclerView adapter minus the boilerplate

		@param context Used to inflate the layout of the ViewHolder

		@param elements The data: Each element is attached to a Generated ViewHolder

		@param updateUi The Parent contains helper methods to make the usage easy,
			   True: the Parent will make a UI Thread test and will call the appropriate notify method
			   False: The task will be performed but the adapter won't be notified


		@see GencyclerRecyclerAdapter: Parent
		@see GencyclerHolder: ViewHolder

		""".trimIndent()

    val ABSTRACT_PERFORM_FILTER_FUNC = """
		The generated custom filter method this will be called when filter is performed


		@param ${Names.CONSTRAINT} filtering query
		@param %L item to check against our filtering query

		@see %T
		@see getFilter
		@see %T

		@return true if the item should be retained and false if the item should be removed.

		""".trimIndent()

    val ABSTRACT_BIND_FUNC = """
		The generated custom onBind method created for [%T]

		@param %L Our generated ViewHolder
		@param %L The current item
		@param position The current position


		@see %T
		@see %T

		""".trimIndent()

    val ABSTRACT_RECYCLE_FUNC = """
		The generated custom onViewRecycled method created for %T

		@param %L Our generated ViewHolder
		@param position The current position


		@see ${Methods.ABSTRACT_BIND_CUSTOM}%L

		""".trimIndent()

    val VIEW_HOLDER_FUNC = """
        Generated ViewHolder created based on [%T.layout.%L]

    """.trimIndent()

    val ON_ACTION_LISTENER_CLASS = """
        The adapter user actions
""".trimIndent()
}

object Packages {
    const val GENCYCLER = "goldzweigapps.com.gencycler"
    const val GENCYCLER_FILTER = "goldzweigapps.com.gencycler.filter"
    const val GENCYCLER_LISTENERS = "goldzweigapps.com.gencycler.listeners"

    const val ANDROID_CONTENT = "android.content"
    const val ANDROID_VIEW = "android.view"
    const val SUPPORT_WIDGET = "android.support.v7.widget"
    const val ANDROID_WIDGET = "android.widget"
    const val SUPPORT_ANNOTATION = "android.support.annotation"
    const val KOTLIN_COLLECTIONS = "kotlin.collections"
}

object Parameters {

    val VIEW_CLASS_NAME = ClassName(Packages.ANDROID_VIEW, "View")

    val MOTION_EVENT_CLASS_NAME = ClassName(Packages.ANDROID_VIEW, "MotionEvent")

    val MOTION_EVENT_PARAMETER_SPEC = ParameterSpec
            .builder(Names.MOTION_EVENT, MOTION_EVENT_CLASS_NAME)
            .build()

    val VIEW_PARAMETER_SPEC = ParameterSpec
            .builder(Names.VIEW, VIEW_CLASS_NAME)
            .build()

    val POSITION_PARAMETER_SPEC = ParameterSpec
            .builder(Names.POSITION, INT)
            .build()

    val VIEW_HOLDER_TYPE_ENUM_CLASS = ClassName(Packages.GENCYCLER,
            "GencyclerHolderViewType")

    val VIEW_HOLDER_SUPER_CLASS = ClassName(Packages.GENCYCLER,
            "GencyclerHolder")

    val GENERIC_DATA_MODEL = GencyclerModel::class.asClassName()

    val GENCYCLER_FILTER = ClassName(Packages.GENCYCLER_FILTER,
            "GencyclerFilter")

    val ILLEGAL_ARGUMENT_EXCEPTION_CLASS =
            IllegalArgumentException::class.asTypeName()

    val CALL_SUPER_ANNOTATION = ClassName(Packages.SUPPORT_ANNOTATION, "CallSuper")
}

object Methods {
    const val GET_VIEW_TYPE = "getItemViewType"
    const val GET_FILTER = "getFilter"
    const val FILTER = "filter"

    const val ON_CREATE_VIEW_HOLDER = "onCreateViewHolder"
    const val ON_BIND_VIEW_HOLDER = "onBindViewHolder"
    const val ON_VIEW_RECYCLED = "onViewRecycled"
    const val PERFORM_FILTER = "performFilter"


    const val ABSTRACT_BIND_CUSTOM = "onBind"
    const val ABSTRACT_RECYCLED_CUSTOM = "onRecycled"
}

object Names {
    const val VIEW = "view"
    const val ELEMENTS = "elements"
    const val ELEMENT = "element"
    const val CONTEXT = "context"
    const val UPDATE_UI = "updateUi"
    const val MOTION_EVENT = "event"
    const val POSITION = "position"
    const val MULTI_LINE_ESCAPED = "\"\"\""
    const val ON_CLICK_LISTENER = "onItemClickListener"
    const val ON_LONG_CLICK_LISTENER = "onItemLongClickListener"
    const val FILTER = "filter"
    const val CONSTRAINT = "constraint"
    const val ACTION_LISTENER = "onActionListener"
}


val widgets = hashSetOf(
        "AbsListView",
        "AbsSeekBar",
        "AbsSpinner",
        "AbsoluteLayout",
        "AccessibilityIterators",
        "ActionMenuPresenter",
        "ActionMenuView",
        "ActivityChooserModel",
        "ActivityChooserView",
        "Adapter",
        "AdapterView",
        "AdapterViewAnimator",
        "AdapterViewFlipper",
        "Advanceable",
        "AlphabetIndexer",
        "AnalogClock",
        "AppSecurityPermissions",
        "ArrayAdapter",
        "AutoCompleteTextView",
        "BaseAdapter",
        "BaseExpandableListAdapter",
        "Button",
        "CalendarView",
        "CalendarViewLegacyDelegate",
        "CalendarViewMaterialDelegate",
        "CheckBox",
        "Checkable",
        "CheckedTextView",
        "Chronometer",
        "CompoundButton",
        "CursorAdapter",
        "CursorFilter",
        "CursorTreeAdapter",
        "DatePicker",
        "DatePickerCalendarDelegate",
        "DatePickerController",
        "DatePickerSpinnerDelegate",
        "DateTimeView",
        "DayPickerPagerAdapter",
        "DayPickerView",
        "DayPickerViewPager",
        "DialerFilter",
        "DigitalClock",
        "DoubleDigitManager",
        "DropDownListView",
        "EdgeEffect",
        "EditText",
        "Editor",
        "ExpandableListAdapter",
        "ExpandableListConnector",
        "ExpandableListPosition",
        "ExpandableListView",
        "FastScroller",
        "Filter",
        "FilterQueryProvider",
        "Filterable",
        "ForwardingListener",
        "FrameLayout",
        "Gallery",
        "GridLayout",
        "GridView",
        "HeaderViewListAdapter",
        "HeterogeneousExpandableList",
        "HorizontalScrollView",
        "ImageButton",
        "ImageSwitcher",
        "ImageView",
        "LinearLayout",
        "ListAdapter",
        "ListPopupWindow",
        "ListView",
        "Magnifier",
        "MediaControlView2",
        "MediaController",
        "MenuItemHoverListener",
        "MenuPopupWindow",
        "MultiAutoCompleteTextView",
        "NumberPicker",
        "OnDateChangedListener",
        "OverScroller",
        "PopupMenu",
        "PopupWindow",
        "ProgressBar",
        "QuickContactBadge",
        "RadialTimePickerView",
        "RadioButton",
        "RadioGroup",
        "RatingBar",
        "RelativeLayout",
        "RemoteViews",
        "RemoteViews",
        "RemoteViewsAdapter",
        "RemoteViewsListAdapter",
        "RemoteViewsService",
        "ResourceCursorAdapter",
        "ResourceCursorTreeAdapter",
        "RtlSpacingHelper",
        "ScrollBarDrawable",
        "ScrollView",
        "Scroller",
        "SearchView",
        "SectionIndexer",
        "SeekBar",
        "SelectionActionModeHelper",
        "ShareActionProvider",
        "SimpleAdapter",
        "SimpleCursorAdapter",
        "SimpleCursorTreeAdapter",
        "SimpleExpandableListAdapter",
        "SimpleMonthView",
        "SlidingDrawer",
        "SmartSelectSprite",
        "Space",
        "SpellChecker",
        "Spinner",
        "SpinnerAdapter",
        "StackView",
        "SuggestionsAdapter",
        "Switch",
        "TabHost",
        "TabWidget",
        "TableLayout",
        "TableRow",
        "TextClock",
        "TextInputTimePickerView",
        "TextSwitcher",
        "TextView",
        "TextViewMetrics",
        "ThemedSpinnerAdapter",
        "TimePicker",
        "TimePickerClockDelegate",
        "TimePickerSpinnerDelegate",
        "Toast",
        "ToggleButton",
        "Toolbar",
        "TwoLineListItem",
        "VideoView",
        "VideoView2",
        "ViewAnimator",
        "ViewFlipper",
        "ViewSwitcher",
        "WrapperListAdapter",
        "YearPickerView",
        "ZoomButton",
        "ZoomButtonsController",
        "ZoomControls")