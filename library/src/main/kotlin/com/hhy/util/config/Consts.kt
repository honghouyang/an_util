package com.hhy.util.config

internal object Consts {
    const val ZIP_EXT = ".zip"

    object Cmd {
        const val SU = "su"
        const val SH = "sh"
        const val EXIT = "exit\n"
        const val LINE_END = "\n"
    }

    object DIGIT {
        val LOWER = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f'
        )

        val UPPER = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F'
        )
    }
}
