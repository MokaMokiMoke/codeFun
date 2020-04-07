package MaxAndMelonSoft;

public class ClientFactory extends AbstractClientFactory {

	@Override
	Client createClient(Identifier id) {
		return new Client(id);
	}

}
