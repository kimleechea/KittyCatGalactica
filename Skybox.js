var JavaPackages = new JavaImporter(
    Packages.ray.rage.scene.SceneManager,
    Packages.ray.rage.scene.Light,
    Packages.ray.rage.scene.Light.Type,
    Packages.ray.rage.scene.Light.Type.POINT,
    Packages.java.awt.Color,
    Packages.ray.rage.asset.texture
);

with (JavaPackages) {
  
    xform.translate(0, front.getImage().getHeight());        
    xform.scale(parseFloat(1), parseFloat(-1));        
   
    front.transform(xform);        
    back.transform(xform);        
    left.transform(xform);        
    right.transform(xform);        
    top.transform(xform);        
    bottom.transform(xform);
        
}

