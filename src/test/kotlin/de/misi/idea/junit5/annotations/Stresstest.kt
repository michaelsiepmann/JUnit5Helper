package de.misi.idea.junit5.annotations

import de.misi.idea.junit5.extensions.StresstestExtension
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(StresstestExtension::class)
annotation class Stresstest