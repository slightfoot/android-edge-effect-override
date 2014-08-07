Android Edge Effect Override
============================

Enables you to override the color of the [Edge Effects](https://github.com/android/platform_frameworks_base/blob/master/core/java/android/widget/EdgeEffect.java) used in your application.

> Author  [Simon Lightfoot](mailto:simon@demondevelopers.com)
>
> Since   30/10/2013
>
> Version 1.4


Can be simply applied with.
* ```EdgeEffectOverride.createContextWrapper(context, color);```
* ```EdgeEffectOverride.createContextThemeWrapper(context, themeResId, color);```

If you want to apply the change Activity wide use:
```
@Override
protected void attachBaseContext(Context newBase)
{
    super.attachBaseContext(EdgeEffectOverride.createContextWrapper(newBase,
        newBase.getResources().getColor(R.color.your_color)));
}
```

<img src="https://raw.github.com/slightfoot/android-edge-effect-override/master/Example.png" alt="Example" width="360" />
