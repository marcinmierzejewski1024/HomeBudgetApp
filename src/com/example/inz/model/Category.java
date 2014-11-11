package com.example.inz.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by dom on 12.10.14.
 */
public class Category
{
    @DatabaseField(generatedId = true)
    long categoryId;
    @DatabaseField()
    Category parentCategory;
    @DatabaseField()
    String name;

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
        this.name = name;
    }

    public Category(String name, Category parentCategory)
    {
        this.name = name;
        this.parentCategory = parentCategory;
    }

    public Category(String name)
    {
        this.name = name;
    }
}
