package reactivecircus.firestorm

import com.google.common.truth.Truth.assertThat
import java.io.File

fun assertFileExists(file: File) {
    assertThat(file.exists()).isTrue()
}
