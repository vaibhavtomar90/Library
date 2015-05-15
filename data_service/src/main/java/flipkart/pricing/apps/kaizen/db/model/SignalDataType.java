package flipkart.pricing.apps.kaizen.db.model;

public enum SignalDataType {

    INT {
        @Override
        public boolean isValid(String value) {
            if (value==null){
                return true;
            }
            try {
                Integer.parseInt(value);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    },
    DOUBLE {
        @Override
        public boolean isValid(String value) {
            if (value==null){
                return true;
            }
            try {
                Double.parseDouble(value);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    },
    FLOAT {
        @Override
        public boolean isValid(String value) {
            if (value==null){
                return true;
            }
            try {
                Float.parseFloat(value);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    },
    LONG {
        @Override
        public boolean isValid(String value) {
            if (value==null){
                return true;
            }
            try {
                Long.parseLong(value);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    },
    STRING {
        @Override
        public boolean isValid(String value) {
            return true;
        }
    },
    PRICE {
        @Override
        public boolean isValid(String value) {
            if (value==null){
                return true;
            }
            try {
                Double.parseDouble(value);
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
        @Override
        public boolean needsQualifier() {
            return true;
        }
    };

    abstract public boolean isValid(String value);

    public boolean needsQualifier() { return false; }

}
