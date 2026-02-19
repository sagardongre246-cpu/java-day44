import java.util.*;

class SecureFragmenter {

    static class Fragment {
        String encryptedData;
        int quantumState;
        int position;

        Fragment(String data, int state, int pos) {
            this.encryptedData = data;
            this.quantumState = state;
            this.position = pos;
        }
    }

    // Dynamic XOR Encryption
    public static String encrypt(String data, int key) {
        StringBuilder sb = new StringBuilder();
        for (char c : data.toCharArray()) {
            sb.append((char) (c ^ key));
        }
        return sb.toString();
    }

    public static String decrypt(String data, int key) {
        return encrypt(data, key); // XOR reversible
    }

    public static List<Fragment> fragmentData(String input, int fragmentSize) {
        List<Fragment> fragments = new ArrayList<>();
        Random random = new Random();

        int position = 0;
        for (int i = 0; i < input.length(); i += fragmentSize) {
            int end = Math.min(i + fragmentSize, input.length());
            String part = input.substring(i, end);

            int quantumState = random.nextInt(100) + 1;
            int dynamicKey = quantumState * 7;

            String encrypted = encrypt(part, dynamicKey);
            fragments.add(new Fragment(encrypted, quantumState, position++));
        }

        Collections.shuffle(fragments); // randomize order
        return fragments;
    }

    public static String reconstructData(List<Fragment> fragments) {
        fragments.sort(Comparator.comparingInt(f -> f.position));

        StringBuilder result = new StringBuilder();

        for (Fragment f : fragments) {
            int dynamicKey = f.quantumState * 7;
            result.append(decrypt(f.encryptedData, dynamicKey));
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter secure message:");
        String message = scanner.nextLine();

        System.out.println("Fragment size:");
        int size = scanner.nextInt();

        List<Fragment> fragments = fragmentData(message, size);

        System.out.println("\n--- Fragmented & Encrypted Data ---");
        for (Fragment f : fragments) {
            System.out.println("Encrypted: " + f.encryptedData +
                               " | QuantumState: " + f.quantumState);
        }

        String reconstructed = reconstructData(fragments);

        System.out.println("\n--- Reconstructed Message ---");
        System.out.println(reconstructed);
    }
}