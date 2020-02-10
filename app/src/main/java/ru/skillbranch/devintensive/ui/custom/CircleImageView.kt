package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.repositories.PreferencesRepository
import kotlin.math.max
import android.os.Parcel
import androidx.annotation.*


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr:Int = 0
): ImageView(context, attrs, defStyleAttr){

    companion object{
        private const val DEFAULT_BORDER_WIDTH = 2
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_SIZE = 40
    }

    @Px
    var borderWidth:Float = context.dpToPx(DEFAULT_BORDER_WIDTH)
    @ColorInt
    private var borderColor:Int = DEFAULT_BORDER_COLOR
    private var initials:String?

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()
    private val borderRect = Rect()
    private var size = 0

//    private var isAvatarMode = true

    private val repository: PreferencesRepository = PreferencesRepository

    init {
        initials = toInitials()
        if(attrs!=null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimension(
                R.styleable.CircleImageView_cv_borderWidth,
                context.dpToPx(DEFAULT_BORDER_WIDTH))
//            initials = a.getString(R.styleable.CircleImageView_cv_initials) ?: "??"
            a.recycle()
        }

        scaleType = ScaleType.CENTER_CROP
        setup()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val initSize = resolveDefaultSize(widthMeasureSpec)
        val maxSize = max(initSize, size)
        setMeasuredDimension(maxSize, maxSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(w == 0) return
        with(viewRect){
            left = 0
            top = 0
            right = w
            bottom = h
        }
        prepareShader(w, h)
    }

    override fun onDraw(canvas: Canvas) {

        if(drawable != null){
            drawAvatar(canvas)
        }else{
            drawInitials(canvas)
        }

        val half = (borderWidth/2).toInt()
        with(borderRect) {
            set(viewRect)
            inset(half, half)
        }
        canvas.drawOval(borderRect.toRectF(), borderPaint)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())
        with(savedState) {
            ssBorderWidth = borderWidth
            ssBorderColor = borderColor
        }
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        super.onRestoreInstanceState(state)
        if (state is SavedState) {
            state.also {
                borderWidth = it.ssBorderWidth
                borderColor = it.ssBorderColor
            }

            with(borderPaint) {
                color = borderColor
                strokeWidth = borderWidth
            }
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
            prepareShader(width,height)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
            prepareShader(width,height)
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        prepareShader(width,height)
    }

    fun updateCircleImageView(){
        initials = toInitials()
        invalidate()
    }


    fun setBorderColor(hex:String){
        borderColor = hex.toInt()
        borderPaint.color = borderColor
        invalidate()
    }

    fun setBorderWidth(@Dimension dp:Int){
        borderWidth = context.dpToPx(dp)
        borderPaint.strokeWidth = borderWidth
        invalidate()
    }

    @Dimension
    fun getBorderWidth():Int{
        return borderWidth.toInt()
    }

    fun getBorderColor():Int{
        return borderColor
    }

    fun setBorderColor(@ColorRes colorId: Int){
        borderColor = colorId
        borderPaint.color = borderColor
        invalidate()
    }

    private fun setup() {
        with(borderPaint){
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = borderColor
        }
    }

    private fun prepareShader(w: Int, h: Int) {
        if(w == 0 || drawable == null) return
        val srcBm = drawable.toBitmap(w,h,Bitmap.Config.ARGB_8888)
        avatarPaint.shader = BitmapShader(srcBm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun resolveDefaultSize(spec:Int):Int{
        return when(MeasureSpec.getMode(spec)){
            MeasureSpec.UNSPECIFIED->context.dpToPx(DEFAULT_SIZE).toInt()
            MeasureSpec.AT_MOST-> MeasureSpec.getSize(spec)
            MeasureSpec.EXACTLY-> MeasureSpec.getSize(spec)
            else->MeasureSpec.getSize(spec)
        }
    }

    private fun Context.dpToPx(dp: Int):Float{
        return dp.toFloat() * this.resources.displayMetrics.density
    }

    private fun drawAvatar(canvas: Canvas){
        canvas.drawOval(viewRect.toRectF(), avatarPaint)
    }

    private fun drawInitials(canvas: Canvas){
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
        @ColorInt val color1 = typedValue.data

        initialsPaint.color = color1
        canvas.drawOval(viewRect.toRectF(), initialsPaint)
        with(initialsPaint){
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33f
        }

        val offsetY = (initialsPaint.descent() + initialsPaint.ascent())/2
        if(initials != null){
            canvas.drawText(initials!!, viewRect.exactCenterX(), viewRect.exactCenterY() - offsetY,initialsPaint)
        }else{
            canvas.drawText("", viewRect.exactCenterX(), viewRect.exactCenterY() - offsetY,initialsPaint)
        }
    }

    private class SavedState : BaseSavedState, Parcelable {
        var ssBorderWidth: Float = 0f
        var ssBorderColor: Int = 0

        constructor(superState: Parcelable?) : super(superState)

        constructor(src: Parcel) : super(src) {
            //restore state from parcel
            ssBorderWidth = src.readFloat()
            ssBorderColor = src.readInt()
        }

        override fun writeToParcel(dst: Parcel, flags: Int) {
            //write state to parcel
            super.writeToParcel(dst, flags)
            dst.writeFloat(ssBorderWidth)
            dst.writeInt(ssBorderColor)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel) = SavedState(parcel)
            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }

     fun toInitials(): String? {
        val firstName = repository.getProfile().firstName
        val lastName = repository.getProfile().lastName
        val firstInitial =
            if (!firstName.isNullOrBlank()) firstName[0].toUpperCase() else null

        val secondInitial =
            if (!lastName.isNullOrBlank()) lastName[0].toUpperCase() else null

        return if (firstInitial == null && secondInitial == null) {
            null
        } else if (firstInitial != null && secondInitial != null) {
            "$firstInitial$secondInitial"
        } else if(firstInitial == null){
            "$secondInitial"
        }else {
            "$firstInitial"
        }
    }
}