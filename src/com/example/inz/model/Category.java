package com.example.inz.model;

import android.graphics.Color;
import com.j256.ormlite.field.DatabaseField;

import java.util.Random;

/**
 * Created by dom on 12.10.14.
 */
public class Category extends AbsDatabaseItem
{
    @DatabaseField(generatedId = true)
    long categoryId;
    @DatabaseField()
    long parentCategoryId;
    @DatabaseField()
    String name;
    @DatabaseField()
    String hexColor;

    Category parentCategory;

    public long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Category getParentCategory()
    {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory)
    {
        this.parentCategory = parentCategory;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        setChanged();
        this.name = name;
    }

    public long getParentCategoryId()
    {
        return parentCategoryId;
    }

    public void setParentCategoryId(long parentCategoryId)
    {
        this.parentCategoryId = parentCategoryId;
    }

    public String getHexColor()
    {
        return hexColor;
    }

    public void setHexColor(String hexColor)
    {
        this.hexColor = hexColor;
    }

    public Category(String name, String hexColor, Category parentCategory)
    {
        this.name = name;
        if(hexColor != null)
            this.hexColor = hexColor;
        else
        {
           this.hexColor = getRandomColor();
        }
            this.parentCategory = parentCategory;
    }

    public Category(String name)
    {
        this(name,null,null);

    }

    public Category()
    {
    }

    public static String getRandomColor() {
        String[] letters = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String color = "#";
        for (int i = 0; i < 6; i++ ) {
            color += letters[(int) Math.round(5+Math.random() * 10)];
        }
        return color;
    }

    public static Category getRestCategory()
    {
        return new Category("Reszta","#555555",null);

    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        if (name != null ? !name.equals(category.name) : category.name != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return name != null ? name.hashCode() : 0;
    }
}
