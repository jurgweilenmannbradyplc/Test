package ch.syseca.sstl.util.obfuscation;


public class ExcludeFromObfuscationTest {

    @ExcludeFromObfuscation
    public static class UnobfuscatedClass {

        @ExcludeFromObfuscation
        protected String property;


        @ExcludeFromObfuscation
        public void setProperty(String property) {
            this.property = property;
        }

    }

}
