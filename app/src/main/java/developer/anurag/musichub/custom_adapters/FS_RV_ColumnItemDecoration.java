package developer.anurag.musichub.custom_adapters;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FS_RV_ColumnItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;
    private final int leftRightSpacing;
    public FS_RV_ColumnItemDecoration(int spacing,int leftRightSpacing){
        this.spacing=spacing;
        this.leftRightSpacing=leftRightSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildAdapterPosition(view)%2==0){
            outRect.right=this.leftRightSpacing;
        }
        else {
        outRect.left=this.leftRightSpacing;

        }
        outRect.top=spacing;

    }
}
