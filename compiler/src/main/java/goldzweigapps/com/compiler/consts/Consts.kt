package goldzweigapps.com.compiler.consts

import com.squareup.kotlinpoet.*
import goldzweigapps.com.annotations.annotations.GencyclerDataContainer

object Packages {
    const val GENCYCLER = "goldzweigapps.com.gencycler"
    const val GENCYCLER_ROOT = "goldzweigapps/com/gencycler"

    const val ANDROID_CONTENT = "android.content"
    const val ANDROID_VIEW = "android.view"
    const val SUPPORT_WIDGET = "android.support.v7.widget"

    const val KOTLIN_COLLECTIONS = "kotlin.collections"
}

object Parameters {
    val VIEW_CLASS_NAME = ClassName(Packages.ANDROID_VIEW, "View")


    val VIEW_PARAMETER_SPEC = ParameterSpec
            .builder(Names.VIEW, VIEW_CLASS_NAME)
            .build()

    val RES_ID_PARAMETER_SPEC = ParameterSpec
            .builder(Names.ID, INT)
            .addAnnotation(ClassName("android.support.annotation", "IdRes"))
            .build()

    val POSITION_PARAMETER_SPEC = ParameterSpec
            .builder(Names.POSITION, INT)
            .build()

    val VIEW_HOLDER_TYPE_ENUM_CLASS = ClassName(Packages.GENCYCLER, "GencyclerHolderViewType")

    val VIEW_HOLDER_SUPER_CLASS = ClassName(Packages.GENCYCLER, "GencyclerHolder")

    val GENERIC_DATA_CONTAINER = GencyclerDataContainer::class.asClassName()

    val ILLEGAL_ARGUMENT_EXCEPTION_CLASS = IllegalArgumentException::class.asTypeName()
}

object Methods {
    const val GET_VIEW_TYPE = "getItemViewType"
    const val GET_ITEM_COUNT = "getItemCount"

    const val ON_CREATE_VIEW_HOLDER = "onCreateViewHolder"
    const val ON_BIND_VIEW_HOLDER = "onBindViewHolder"
    const val ABSTRACT_BIND_CUSTOM = "onBind"
}

object Names {
    const val VIEW = "view"
    const val ELEMENTS = "elements"
    const val CONTEXT = "context"
    const val UPDATE_UI = "updateUi"
    const val ID = "id"
    const val POSITION = "position"
    const val MULTI_LINE_ESCAPED = "\"\"\""
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