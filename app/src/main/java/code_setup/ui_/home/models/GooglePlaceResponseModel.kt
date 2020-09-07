package code_setup.ui_.home.models

data class GooglePlaceResponseModel(
    var predictions: List<Prediction>,
    var status: String
) {
    data class Prediction(
        var description: String,
        var id: String,
        var laat: Double,
        var lnng: Double,
        var matched_substrings: List<MatchedSubstring>,
        var place_id: String,
        var reference: String,
        var structured_formatting: StructuredFormatting,
        var terms: List<Term>,
        var types: List<String>
    ) {
        data class MatchedSubstring(
            var length: Int,
            var offset: Int
        )

        data class StructuredFormatting(
            var main_text: String,
            var main_text_matched_substrings: List<MainTextMatchedSubstring>,
            var secondary_text: String,
            var secondary_text_matched_substrings: List<SecondaryTextMatchedSubstring>
        ) {
            data class MainTextMatchedSubstring(
                var length: Int,
                var offset: Int
            )

            data class SecondaryTextMatchedSubstring(
                var length: Int,
                var offset: Int
            )
        }

        data class Term(
            var offset: Int,
            var value: String
        )
    }
}