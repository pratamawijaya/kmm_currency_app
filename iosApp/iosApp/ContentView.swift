import SwiftUI
import shared

struct Currency: Decodable {
    let code: String
    let name: String
}

struct Rate: Decodable {
    let code: String
    let rate: Double
}

struct ContentView: View {
    @State private var inputValue: String = ""
    @State private var selectedCurrency: String = "USD"
    //@State private var currencies: [Currency] = []
    let currencies = ["USD", "EUR", "IDR"]
    let conversionRates = [1.0, 0.903933, 14726.45]

    var body: some View {
        VStack {
            TextField("Enter value", text: $inputValue)
                .padding()
                .keyboardType(.decimalPad)
                .border(Color.gray, width: 1)

            Picker("Currency", selection: $selectedCurrency) {
                ForEach(currencies, id: \.self) {
                    Text($0)
                }
            }
            .pickerStyle(MenuPickerStyle())
            .padding()

            List {
                ForEach(currencies, id: \.self) { currency in
                    if let rateIndex = currencies.firstIndex(of: currency),
                       let rate = conversionRates[safe: rateIndex],
                       let inputValueDouble = Double(inputValue),
                       let convertedValue = CurrencyConverter.convert(amount: inputValueDouble, from: "USD", to: currency, rates: conversionRates) {
                        Text("\(currency): \(convertedValue, specifier: "%.2f")")
                    }
                }
            }
            .listStyle(InsetGroupedListStyle())
            .navigationTitle("Currency Converter")
        }
    }
}

struct CurrencyConverter {
    static func convert(amount: Double, from: String, to: String, rates: [Double]) -> Double? {
        guard let fromIndex = CurrencyConverter.currencyIndex(for: from),
              let toIndex = CurrencyConverter.currencyIndex(for: to),
              fromIndex < rates.count,
              toIndex < rates.count,
              fromIndex != toIndex
        else { return nil }

        let rate = rates[toIndex] / rates[fromIndex]
        return amount * rate
    }

    private static func currencyIndex(for currency: String) -> Int? {
        switch currency {
        case "USD":
            return 0
        case "EUR":
            return 1
        case "IDR":
            return 2
        default:
            return nil
        }
    }
}

extension Collection {
    subscript(safe index: Index) -> Element? {
        return indices.contains(index) ? self[index] : nil
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}