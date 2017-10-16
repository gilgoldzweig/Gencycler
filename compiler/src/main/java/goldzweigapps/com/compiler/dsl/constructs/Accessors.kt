package goldzweigapps.com.compiler.dsl.constructs

import com.squareup.kotlinpoet.KModifier


interface IAccessor{
    val public      : IAccessor
    val private     : IAccessor
    val protected   : IAccessor
    val internal    : IAccessor
    val operator    : IAccessor
    val open        : IAccessor
    val abstract    : IAccessor
    val inline      : IAccessor
    val const       : IAccessor
    val inner       : IAccessor
    val annotation  : IAccessor
    val data        : IAccessor
    val enum        : IAccessor
    val external    : IAccessor
    val final       : IAccessor
    val infix       : IAccessor
    val lateInit    : IAccessor
    val override    : IAccessor
    val sealed      : IAccessor
    val suspend     : IAccessor
    val tailRec     : IAccessor
    val list        : Array<KModifier>
}

class Accessor internal constructor(private val accessors: MutableSet<KModifier> = mutableSetOf()): IAccessor {
    override val abstract   get() = getValue(KModifier.ABSTRACT)
    override val const      get() = getValue(KModifier.CONST)
    override val annotation get() = getValue(KModifier.ANNOTATION)
    override val data       get() = getValue(KModifier.DATA)
    override val enum       get() = getValue(KModifier.ENUM)
    override val external   get() = getValue(KModifier.EXTERNAL)
    override val final      get() = getValue(KModifier.FINAL)
    override val infix      get() = getValue(KModifier.INFIX)
    override val lateInit   get() = getValue(KModifier.LATEINIT)
    override val override   get() = getValue(KModifier.OVERRIDE)
    override val sealed     get() = getValue(KModifier.SEALED)
    override val suspend    get() = getValue(KModifier.SUSPEND)
    override val tailRec    get() = getValue(KModifier.TAILREC)
    override val open       get() = getValue(KModifier.OPEN)
    override val inner      get() = getValue(KModifier.INNER)
    override val public     get() = getValue(KModifier.PUBLIC)
    override val private    get() = getValue(KModifier.PRIVATE)
    override val protected  get() = getValue(KModifier.PROTECTED)
    override val internal   get() = getValue(KModifier.INTERNAL)
    override val inline     get() = getValue(KModifier.INLINE)
    override val operator   get() = getValue(KModifier.OPERATOR)

    fun getValue(modifier: KModifier) : IAccessor {
        accessors += modifier
        return this
    }
    override val list get() = accessors.toTypedArray()

    companion object{
        val abstract   by lazy { Accessor().abstract   }
        val const      by lazy { Accessor().const      }
        val annotation by lazy { Accessor().annotation }
        val data       by lazy { Accessor().data       }
        val enum       by lazy { Accessor().enum       }
        val external   by lazy { Accessor().external   }
        val final      by lazy { Accessor().final      }
        val infix      by lazy { Accessor().infix      }
        val lateInit   by lazy { Accessor().lateInit   }
        val override   by lazy { Accessor().override   }
        val sealed     by lazy { Accessor().sealed     }
        val suspend    by lazy { Accessor().suspend    }
        val tailRec    by lazy { Accessor().tailRec    }
        val open       by lazy { Accessor().open       }
        val inner      by lazy { Accessor().inner      }
        val public     by lazy { Accessor().public     }
        val private    by lazy { Accessor().private    }
        val protected  by lazy { Accessor().protected  }
        val internal   by lazy { Accessor().internal   }
        val inline     by lazy { Accessor().inline     }
        val operator   by lazy { Accessor().operator   }
    }
}