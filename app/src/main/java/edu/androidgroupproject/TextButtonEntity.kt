package edu.androidgroupproject

class TextButtonEntity : TextEntity() {
    protected var nextState: String? = null


    override fun OnClickFunction() {
        paint.color = onclickColor
        StateManager.Companion.Instance.ChangeState(nextState)
    }

    override fun OffClickFunction() {
        paint.color = defaultColor
    }

    companion object {
        fun Create(
            xPos: Float,
            yPos: Float,
            text: String?,
            textSize: Float,
            _nextState: String?
        ): TextButtonEntity {
            val result = TextButtonEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.SetText(text)
            result.nextState = _nextState
            result.paint.textSize = textSize
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}
